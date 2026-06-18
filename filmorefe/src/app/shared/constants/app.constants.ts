export const VIDEO_CATEGORIES = [
  'Action',
  'Drama',
  'Comedy',
  'Sci-Fi',
  'Thriller',
  'Documentary',
  'Horror',
  'Romance',
  'Animation',
  'Fantasy',
  'Crime',
  'Adventure',
  'Mystery',
  'Family',
  'History',
  'Music',
  'War',
  'Western',
  'Biography',
];

export const RATINGS = ['G', 'PG', 'PG-13', 'R', 'NC-17'];

export const DIALOG_CONFIG = {
  VIDEO_PLAYER: {
    width: '100vw',
    height: '100vh',
    maxWidth: '100vw',
    maxHeight: '100vh',
    panelClass: 'video-player-dialog',
    hasBackdrop: true,
    disableClose: false,
  },
  CHANGE_PASSWORD: {
    width: '600px',
    maxWidth: '90vw',
    panelClass: 'user-dialog',
    hasBackdrop: true,
    disableClose: false,
  },
  CONFIRM: {
    width: '500px',
    panelClass: 'custom-dialog-container',
    hasBackdrop: true,
    disableClose: false,
  },
  MANAGE_USER: {
    width: '600px',
    maxWidth: '90vw',
    panelClass: 'user-dialog',
    hasBackdrop: true,
    disableClose: false,
  },
  VIDEO_FORM: {
    width: '95vw',
    maxWidth: '1400px',
    height: 'auto',
    panelclass: 'video-form-dialog',
    hasBackdrop: true,
    disableClose: false,
  },
};
