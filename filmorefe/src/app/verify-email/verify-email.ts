import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthService } from '../shared/services/auth-service';

@Component({
  selector: 'app-verify-email',
  standalone: false,
  templateUrl: './verify-email.html',
  styleUrl: './verify-email.css',
})
export class VerifyEmail implements OnInit {
  loading = true;
  success = false;
  message = '';

  constructor(
    private route: ActivatedRoute,
    private authService: AuthService,
    private cdr: ChangeDetectorRef,
  ) {}

  // A method that take the token from the Url and then sending it the API to verfy the email
  // and then show the message to the user if the email is verified or not
  ngOnInit(): void {
    console.log('Full URL:', window.location.href);
    console.log('Query params:', this.route.snapshot.queryParamMap.keys);
    console.log('Token from query:', this.route.snapshot.queryParamMap.get('token'));

    const token = this.route.snapshot.queryParamMap.get('token');

    if (!token) {
      this.loading = false;
      this.success = false;
      this.message = 'Invalid verification link. No token provided.';
      return;
    }

    this.authService.verifyEmail(token).subscribe({
      next: (response: any) => {
        this.loading = false;
        this.success = true;
        this.message = response.message || 'Email verified successfully! You can now login.';
        this.cdr.detectChanges();
      },

      error: (err) => {
        console.log('Full error object:', err);
        console.log('err.error:', err.error);
        this.loading = false;
        this.success = false;
        this.message = err.error?.message || 'Failed to verify email. Please try again.';
        this.cdr.detectChanges();
      },
    });
  }
}
