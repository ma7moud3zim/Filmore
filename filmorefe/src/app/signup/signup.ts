import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthService } from '../shared/services/auth-service';
import { ErrorHandlerService } from '../shared/services/error-handler-service';
import { NotificationService } from '../shared/services/notification-service';

@Component({
  selector: 'app-signup',
  standalone: false,
  templateUrl: './signup.html',
  styleUrl: './signup.css',
})
export class Signup implements OnInit {
  hidePassword = true;
  hideConfirmPassword = true;
  signupForm!: FormGroup;
  loading = false;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute,
    private notification: NotificationService,
    private errorHandlerService: ErrorHandlerService,
  ) {
    this.signupForm = this.fb.group({
      fullName: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: [
        '',
        [Validators.required, this.authService.passwordMatchValidator('password')],
      ],
    });
  }

  ngOnInit(): void {
    if (this.authService.isLoggedIn()) {
      this.authService.redirectBasedOnRole();
    }
    const email = this.route.snapshot.queryParamMap.get('email');
    if (email) {
      this.signupForm.patchValue({ email: email });
      console.log(email);
    }
  }

  submit() {
    this.loading = true;
    const formData = this.signupForm.value;
    const data = {
      email: formData.email?.trim().toLowerCase(),
      password: formData.password,
      fullName: formData.fullName,
    };
    this.authService.signup(data).subscribe({
      next: (response: any) => {
        this.loading = false;
        this.notification.success(response?.message);
        this.router.navigate(['/login']);
      },
      error: (error) => {
        this.loading = false;
        this.errorHandlerService.handle(error, 'Signup failed. Please try again.');
      },
    });
  }
}
