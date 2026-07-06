import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserService } from '../../../shared/services/user-service';
import { NotificationService } from '../../../shared/services/notification-service';
import { ErrorHandlerService } from '../../../shared/services/error-handler-service';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-manage-user',
  standalone: false,
  templateUrl: './manage-user.html',
  styleUrl: './manage-user.css',
})
export class ManageUser {
  userForm!: FormGroup;
  creating = false;
  hidePassword = true;
  isEditMode = false;
  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private notification: NotificationService,
    private errorHandlerService: ErrorHandlerService,
    public dialogRef: MatDialogRef<ManageUser>,
    @Inject(MAT_DIALOG_DATA) public data: any,
  ) {
    this.isEditMode = data.mode === 'edit';

    this.userForm = this.fb.group({
      FullName: [data.user?.fullName || '', [Validators.required]],
      email: [data.user?.email || '', [Validators.required, Validators.email]],
      password: ['', this.isEditMode ? [] : [Validators.required, Validators.minLength(6)]],
      role: [data.user?.role || 'USER', [Validators.required]],
    });
  }

  onCancel() {
    this.dialogRef.close();
  }

  onSave() {
    this.creating = true;
    const formData = this.userForm.value;
    const data = {
      email: formData.email?.trim().toLowerCase(),
      password: formData.password,
      fullName: formData.FullName,
      role: formData.role,
    };

    const op$ = this.isEditMode
      ? this.userService.updateUser(this.data.user.id, data)
      : this.userService.createUser(data);

    op$.subscribe({
      next: (response: any) => {
        this.creating = false;
        this.notification.success(
          this.isEditMode ? 'User updated successfully' : 'User created successfully',
        );
        this.dialogRef.close(true);
      },
      error: (error) => {
        this.creating = false;
        this.errorHandlerService.handle(
          error,
          this.isEditMode ? 'Failed to update user' : 'Failed to create user',
        );
      },
    });
  }
}
