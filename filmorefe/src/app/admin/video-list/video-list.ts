import { ChangeDetectorRef, Component, HostListener, OnInit } from '@angular/core';
import { DialogService } from '../../shared/services/dialog-service';
import { MatTableDataSource } from '@angular/material/table';
import { NotificationService } from '../../shared/services/notification-service';
import { VideoService } from '../../shared/services/video-service';
import { UtilityService } from '../../shared/services/utility-service';
import { MediaService } from '../../shared/services/media-service';
import { ErrorHandlerService } from '../../shared/services/error-handler-service';

@Component({
  selector: 'app-video-list',
  standalone: false,
  templateUrl: './video-list.html',
  styleUrl: './video-list.css',
})
export class VideoList implements OnInit {
  pagedVideos: any = [];
  loading = false;
  loadingMore = false;
  searchQuery = '';

  pageSize = 10;
  currentPage = 0;
  totalPage = 0;
  totalElements = 0;
  hasMoreVideos = true;

  totalVideos = 0;
  publishedVideos = 0;
  totalDurationSeconds = 0;
  // data = new MatTableDataSource<any>();

  constructor(
    private dialogService: DialogService,
    private notification: NotificationService,
    private videoService: VideoService,
    public utilityService: UtilityService,
    public mediaService: MediaService,
    private errorHandler: ErrorHandlerService,
    private cdr: ChangeDetectorRef,
  ) {}

  ngOnInit() {
    this.load();
    this.loadStats();
  }

  @HostListener('window:scroll')
  onScroll(): void {
    const scrollPosition = window.pageYOffset + window.innerHeight;
    const pageHeight = document.documentElement.scrollHeight;
    if (scrollPosition >= pageHeight - 200 && !this.loadingMore && this.hasMoreVideos) {
      this.loadMoreVideos();
    }
  }
  load() {
    this.loading = true;
    this.currentPage = 0;
    this.pagedVideos = [];
    const search = this.searchQuery.trim() || undefined;

    this.videoService.getAllAdminVideos(this.currentPage, this.pageSize, search).subscribe({
      next: (response: any) => {
        this.loading = false;
        this.pagedVideos = response.content;
        this.totalPage = response.totalPages;
        this.totalElements = response.totalElements;
        this.hasMoreVideos = this.currentPage < this.totalPage - 1;
        // this.data.data = this.pageVideos;
        this.cdr.markForCheck();
      },
      error: (error) => {
        this.loadingMore = false;
        this.errorHandler.handle(error, 'Error loading videos');
        this.cdr.markForCheck();
      },
    });
  }
  loadMoreVideos() {
    if (this.loadingMore || !this.hasMoreVideos) return;
    this.loadingMore = true;
    const nextPage = this.currentPage + 1;
    const search = this.searchQuery.trim() || undefined;

    this.videoService.getAllAdminVideos(nextPage, this.pageSize, search).subscribe({
      next: (response: any) => {
        this.loadingMore = false;
        this.currentPage = nextPage;
        this.pagedVideos = [...this.pagedVideos, ...response.content];
        this.totalPage = response.totalPages;
        this.totalElements = response.totalElements;
        this.hasMoreVideos = this.currentPage < this.totalPage - 1;
        // this.data.data = this.pageVideos;
        this.cdr.markForCheck();
      },

      error: (error) => {
        this.loadingMore = false;
        this.errorHandler.handle(error, 'Error loading videos');
        this.cdr.markForCheck();
      },
    });
  }
  loadStats() {
    this.videoService.getStatsByAdmin().subscribe((stats: any) => {
      this.totalVideos = stats.totalVideos;
      this.publishedVideos = stats.publishedVideos;
      this.totalDurationSeconds = stats.totalDuration;
    });
  }
  onSearchChange(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.searchQuery = input.value;
    this.currentPage = 0;
    this.load();
  }
  clearSearch() {
    this.searchQuery = '';
    this.currentPage = 0;
    this.load();
  }

  play(video: any) {
    this.dialogService.openVideoPlayer(video);
  }
  createNew() {
    const dialogRef = this.dialogService.openVideoFormDialog('create');
    dialogRef.afterClosed().subscribe((response) => {
      if (response) {
        this.notification.success('Video created successfully');
        this.load();
        this.loadStats();
      }
    });
  }

  edit(video: any) {
    const dialogRef = this.dialogService.openVideoFormDialog('edit', video);
    dialogRef.afterClosed().subscribe((response) => {
      if (response) {
        this.notification.success('Video updated successfully');
        this.load();
        this.loadStats();
      }
    });
  }

  remove(video: any) {
    this.dialogService
      .openConfirmation(
        'Delete Video',
        `Are you sure you want to delete "${video.title}" video?`,
        'Delete',
        'Cancel',
      )
      .subscribe((response) => {
        if (response) {
          this.loading = true;
          this.videoService.deleteVideoByAdmin(video.id).subscribe({
            next: (response: any) => {
              this.notification.success(response.message || 'Video deleted successfully');
              this.load();
              this.loadStats();
            },
            error: (error) => {
              this.loading = false;
              this.errorHandler.handle(error, 'Error deleting video, Please try again.');
            },
          });
        }
      });
  }

  togglePublish(event: any, video: any) {
    const newPublishedState = event.checked;
    this.videoService.setPublishedByAdmin(video.id, newPublishedState).subscribe({
      next: (response: any) => {
        video.published = newPublishedState;
        this.notification.success(
          `Video ${video.published ? 'published' : 'unpublished'} successfully`,
        );
        if (newPublishedState) {
          this.publishedVideos++;
        } else {
          this.publishedVideos--;
        }
        this.loadStats();
      },
      error: (error) => {
        event.source.checked = !newPublishedState;
        video.published = !newPublishedState;
        this.errorHandler.handle(error, 'Error updating video published state, Please try again.');
      },
    });
  }

  getPublishedCount(): number {
    return this.publishedVideos;
  }

  getTotalDuration(): string {
    const hours = Math.floor(this.totalDurationSeconds / 3600);
    const mins = Math.floor((this.totalDurationSeconds % 3600) / 60);
    if (hours > 0) return `${hours}h ${mins}m`;
    return `${mins}m`;
  }

  formatDuration(seconds: number): string {
    return this.utilityService.formatDuration(seconds);
  }

  getPosterUrl(video: any) {
    const url = this.mediaService.getMediaUrl(video, 'image', { useCache: true });
    return url;
  }
}
