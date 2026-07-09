import { Component, HostListener, OnDestroy, OnInit } from '@angular/core';
import { debounceTime, distinctUntilChanged, Subject } from 'rxjs';
import { VideoService } from '../../shared/services/video-service';
import { WatchlistService } from '../../shared/services/watchlist-service';
import { NotificationService } from '../../shared/services/notification-service';
import { UtilityService } from '../../shared/services/utility-service';
import { MediaService } from '../../shared/services/media-service';
import { DialogService } from '../../shared/services/dialog-service';
import { ErrorHandlerService } from '../../shared/services/error-handler-service';

@Component({
  selector: 'app-my-favorites',
  standalone: false,
  templateUrl: './my-favorites.html',
  styleUrl: './my-favorites.css',
})
export class MyFavorites implements OnInit, OnDestroy {
  allVideos: any = [];
  filteredVideos: any = [];
  loading = true;
  loadingMore = false;
  error = false;
  searchQuery: string = '';

  currentPage = 0;
  pageSize = 10;
  totalElements = 0;
  totalPages = 0;
  hasMoreVideos = true;

  private searchSubject = new Subject<string>();

  constructor(
    private videoService: VideoService,
    private watchlistService: WatchlistService,
    private notification: NotificationService,
    public utility: UtilityService,
    public mediaService: MediaService,
    public dialogService: DialogService,
    private errorHandler: ErrorHandlerService,
  ) {}

  ngOnInit(): void {
    this.loadVideos();
    this.initializeSearchDebounce();
  }
  ngOnDestroy(): void {
    this.searchSubject.complete();
  }

  initializeSearchDebounce(): void {
    this.searchSubject.pipe(debounceTime(300), distinctUntilChanged()).subscribe(() => {
      this.performSearch();
    });
  }

  @HostListener('window:scroll')
  onScroll(): void {
    const scrollPosition = window.pageYOffset + window.innerHeight;
    const pageHeight = document.documentElement.scrollHeight;
    if (scrollPosition >= pageHeight - 200 && !this.loadingMore && this.hasMoreVideos) {
      this.loadMoreVideos();
    }
  }

  loadVideos(page: number = 0) {
    this.error = false;
    this.currentPage = 0;
    this.allVideos = [];
    this.filteredVideos = [];

    const search = this.searchQuery.trim() || undefined;
    this.loading = true;

    this.watchlistService.getWatchlist(page, this.pageSize, search).subscribe({
      next: (response: any) => {
        this.allVideos = response.content;
        this.filteredVideos = this.allVideos;
        this.totalElements = response.totalElements;
        this.totalPages = response.totalPages;
        this.hasMoreVideos = this.currentPage < this.totalPages - 1;
        this.loading = false;
      },
      error: (error) => {
        this.error = true;
        this.loadingMore = false;
        this.errorHandler.handle(error, 'Error loading videos');
        console.error('Error loading videos:', error);
      },
    });
  }
  loadMoreVideos() {
    if (this.loadingMore || !this.hasMoreVideos) return;
    this.loadingMore = true;
    const nextPage = this.currentPage + 1;
    const search = this.searchQuery.trim() || undefined;

    this.watchlistService.getWatchlist(nextPage, this.pageSize, search).subscribe({
      next: (response: any) => {
        this.loadingMore = false;
        this.currentPage = response.number;
        this.allVideos = [...this.allVideos, ...response.content];
        this.filteredVideos = [...this.filteredVideos, ...response.content];
        this.totalPages = response.totalPages;
        this.totalElements = response.totalElements;
        this.hasMoreVideos = this.currentPage < this.totalPages - 1;
      },

      error: (error) => {
        this.notification.error('Error loading videos');
        this.loadingMore = false;
        this.errorHandler.handle(error, 'Error loading videos');
      },
    });
  }

  onSearch() {
    this.searchSubject.next(this.searchQuery);
  }

  private performSearch() {
    this.currentPage = 0;
    this.loadVideos();
  }

  clearSearch() {
    this.searchQuery = '';
    this.currentPage = 0;
    this.loadMoreVideos();
  }

  toggleWatchlist(video: any, event?: Event) {
    if (event) {
      event.stopPropagation();
    }

    const videoId = video.id!;
    this.watchlistService.removeFromWatchlist(videoId).subscribe({
      next: () => {
        this.allVideos = this.allVideos.filter((v: any) => v.id !== videoId);
        this.filteredVideos = this.filteredVideos.filter((v: any) => v.id !== videoId);
        this.notification.success('Video removed from favorites');
      },
      error: (err) => {
        this.errorHandler.handle(err, 'Error removing video from favorites');
      },
    });
  }

  getPosterUrl(video: any): string {
    return this.mediaService.getMediaUrl(video, 'image', { useCache: true }) || '';
  }

  playVideo(video: any) {
    this.dialogService.openVideoPlayer(video);
  }

  formatDuration(seconds: number | undefined): string {
    return this.utility.formatDuration(seconds);
  }
}
