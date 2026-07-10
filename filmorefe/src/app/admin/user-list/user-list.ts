import { ChangeDetectorRef, Component, HostListener, OnInit } from '@angular/core';
import { UserService } from '../../shared/services/user-service';
import { DialogService } from '../../shared/services/dialog-service';
import { AuthService } from '../../shared/services/auth-service';
import { NotificationService } from '../../shared/services/notification-service';
import { ErrorHandlerService } from '../../shared/services/error-handler-service';

@Component({
  selector: 'app-user-list',
  standalone: false,
  templateUrl: './user-list.html',
  styleUrl: './user-list.css',
})
export class UserList implements OnInit {
  paginatedUsers: any = [];
  loading = true;
  loadingMore = false;
  error = false;
  currentUserEmail: string | null = null;
  searchQuery: string = '';
  pageSize = 4;
  currentPage = 0;
  totalPage = 0;
  totalUsers = 0;
  hasMoreUsers = true;

  constructor(
    private userService: UserService,
    private authService: AuthService,
    private dialogService: DialogService,
    private notification: NotificationService,
    private errorHandler: ErrorHandlerService,
    private cdr: ChangeDetectorRef,
  ) {}

  ngOnInit() {
    const currentUser = this.authService.getCurrentUser();
    this.currentUserEmail = currentUser?.email || null;
    this.loadUsers();
  }

  @HostListener('window:scroll')
  onScroll(): void {
    const scrollPosition = window.pageYOffset + window.innerHeight;
    const pageHeight = document.documentElement.scrollHeight;
    if (scrollPosition >= pageHeight - 200 && !this.loadingMore && this.hasMoreUsers) {
      this.loadMoreUsers();
    }
  }

  loadUsers() {
    this.loading = true;
    this.error = false;
    this.currentPage = 0;
    this.paginatedUsers = [];
    const search = this.searchQuery.trim() || undefined;

    this.userService.getAllUsers(this.currentPage, this.pageSize, search).subscribe({
      next: (response: any) => {
        this.paginatedUsers = response.content;
        this.totalPage = response.totalPages;
        this.totalUsers = response.totalElements;
        this.currentPage = response.number;
        this.hasMoreUsers = this.currentPage < this.totalPage - 1;
        this.loading = false;
        this.cdr.markForCheck();
      },
      error: (error) => {
        this.error = true;
        this.loadingMore = false;
        this.errorHandler.handle(error, 'Error loading users');
        this.cdr.markForCheck();
      },
    });
  }

  loadMoreUsers() {
    if (this.loadingMore || !this.hasMoreUsers) return;
    this.loadingMore = true;
    const nextPage = this.currentPage + 1;
    const search = this.searchQuery.trim() || undefined;

    this.userService.getAllUsers(nextPage, this.pageSize, search).subscribe({
      next: (response: any) => {
        this.paginatedUsers = [...this.paginatedUsers, ...response.content];
        this.currentPage = response.number;
        this.hasMoreUsers = this.currentPage < this.totalPage - 1;
        this.loadingMore = false;
        this.cdr.markForCheck();
      },
      error: (error) => {
        this.loadingMore = false;
        this.errorHandler.handle(error, 'Failed loading more users');
        this.cdr.markForCheck();
      },
    });
  }

  onSearchChange(event: Event) {
    const input = event.target as HTMLInputElement;
    this.searchQuery = input.value;
    this.currentPage = 0;
    this.loadUsers();
  }

  clearSearch() {
    this.searchQuery = '';
    this.currentPage = 0;
    this.loadUsers();
  }

  createUser() {
    const dialogRef = this.dialogService.openManageUserDialog('create');
    dialogRef.afterClosed().subscribe((response) => {
      if (response) {
        this.notification.success('User created successfully');
        this.loadUsers();
      }
    });
  }

  editUser(user: any) {
    const dialogRef = this.dialogService.openManageUserDialog('edit', user);
    dialogRef.afterClosed().subscribe((response) => {
      if (response) {
        this.notification.success('User updated successfully');
        this.loadUsers();
      }
    });
  }

  isCurrentUser(user: any): boolean {
    return user.email === this.currentUserEmail;
  }
  toggleUserStatus(user: any): void {
    this.userService.toggleUserStatus(user.id).subscribe({
      next: (response: any) => {
        this.notification.success(response.message);
        this.loadUsers();
      },
      error: (error) => {
        this.errorHandler.handle(error, 'Failed to toggle user status');
      },
    });
  }

  deleteUser(user: any) {
    if (this.isCurrentUser(user)) {
      this.notification.error('You cannot delete your own account');
      return;
    }

    const dialogRef = this.dialogService
      .openConfirmation(
        'Delete User',
        `Are you sure you want to delete the user "${user.name}"?`,
        'Delete',
        'Cancel',
        'danger',
      )
      .subscribe((confirmed) => {
        if (confirmed) {
          this.userService.deleteUser(user.id).subscribe({
            next: () => {
              this.notification.success('User deleted successfully');
              this.loadUsers();
            },
            error: (error) => {
              this.errorHandler.handle(error, 'Failed to delete user');
            },
          });
        }
      });
  }

  changeUserRole(user: any) {
    const newRole = user.role === 'ADMIN' ? 'USER' : 'ADMIN';
    this.dialogService
      .openConfirmation(
        'Change User Role',
        `Are you sure you want to change the role of "${user.name}" to "${newRole}"?`,
        'Change Role',
        'Cancel',
        'warning',
      )
      .subscribe((response) => {
        if (response) {
          this.userService.changeUserRole(user.id, newRole).subscribe({
            next: () => {
              this.notification.success(`User role changed to ${newRole} successfully`);
              this.loadUsers();
            },
            error: (error) => {
              this.errorHandler.handle(error, 'Failed to change user role');
            },
          });
        }
      });
  }

  getRoleBadgeClass(role: string): string {
    return role === 'ADMIN' ? 'role-badge admin' : 'role-badge user';
  }

  getStatusBadgeClass(active: boolean): string {
    return active ? 'status-badge active' : 'status-badge inactive';
  }

  formatDate(dateString: string): string {
    const date = new Date(dateString);
    return date.toLocaleDateString('en-us', { year: 'numeric', month: 'short', day: 'numeric' });
  }
}
