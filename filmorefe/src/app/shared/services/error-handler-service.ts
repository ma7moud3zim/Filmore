import { Injectable } from '@angular/core';
import { NotificationService } from './notification-service';

@Injectable({
  providedIn: 'root',
})
export class ErrorHandlerService {
  constructor(private notification: NotificationService) {}

  // This method is used to handle errors and display a notification to the user.
  handle(error: any, fullbackMessage: string) {
    const errorMsg = error.error?.error || fullbackMessage;
    this.notification.error(errorMsg);
    console.error(error);
  }
}
