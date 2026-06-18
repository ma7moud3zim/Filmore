import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';
import { BehaviorSubject, filter, take, tap } from 'rxjs';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiUrl = environment.apiUrl + '/auth';

  private currentUserSubject = new BehaviorSubject<any>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(
    private http: HttpClient,
    private router: Router,
  ) {}

  passwordMatchValidator(passwordControlName: string): ValidatorFn {
    return (confirmControl: AbstractControl): ValidationErrors | null => {
      if (!confirmControl.parent) return null;
      const passwordControl = confirmControl.parent?.get(passwordControlName);
      if (!passwordControl) {
        return null; // Can't validate if the password control is not found
      }
      const password = passwordControl.value;
      const confirmPassword = confirmControl.value;

      // So if the password is equal to the confirm password, then we return null, which means no error.
      // Otherwise, we return an object with a key of passwordMismatch as true, which is a validation error.
      return password === confirmPassword ? null : { passwordMismatch: true };
    };
  }

  // The signup method takes the data of signing up, and sends a POST request to the /signup endpoint of the API.
  // It returns the observable from the HTTP request.
  signup(signUpData: any) {
    return this.http.post(`${this.apiUrl}/signup`, signUpData);
  }

  // The verify email method takes a token as a parameter,
  // and sends a GET request to the /verify-email endpoint of the API with the token as a query parameter.
  verifyEmail(token: string) {
    return this.http.get(this.apiUrl + '/verify-email?token=' + token);
  }

  // a method takes login data and sends a Post request to the /login endpoint of the API
  // and returns the observable from the HTTP request
  login(loginData: any) {
    return this.http
      .post(`${this.apiUrl}/login`, loginData)
      .pipe(tap((response) => this.handleAuthSuccess(response)));
  }

  handleAuthSuccess(authData: any) {
    if (authData?.token) {
      localStorage.setItem('token', authData.token);
    }
    this.setCurrentUser(authData);
  }

  setCurrentUser(user: any) {
    this.currentUserSubject.next(user);
  }

  getCurrentUser() {
    return this.currentUserSubject.value;
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  isAuthenticated() {
    return !!this.getToken();
  }

  getRole() {
    const user = this.getCurrentUser();
    return user?.role;
  }

  isLoggedIn(): Boolean {
    return !!this.getToken();
  }
  redirectBasedOnRole() {
    if (!this.isAuthenticated()) {
      this.router.navigate(['/login']);
      return;
    }

    const user = this.getCurrentUser();
    if (!user) {
      this.currentUser$
        .pipe(
          filter((u) => u !== null),
          take(1),
        )
        .subscribe(() => this.redirectBasedOnRole());
      return;
    }
    const targetUrl = this.isAdmin() ? '/admin' : '/home';
    this.router.navigate([targetUrl]);
  }

  isAdmin(): boolean {
    const user = this.getCurrentUser();
    return user?.role === 'ADMIN';
  }

  resendVerificationEmail(email: string) {
    return this.http.post(this.apiUrl + '/resend-verification', { email });
  }

  forgotPassword(email: string) {
    return this.http.post(this.apiUrl + '/forget-password', { email });
  }

  resetPassword(token: string, newPassword: string) {
    return this.http.post(this.apiUrl + '/reset-password', { token, newPassword });
  }

  initializeAuth(): Promise<void> {
    return new Promise((resolve) => {
      if (!this.isLoggedIn()) {
        this.handleAuthSuccess(null);
        resolve();
        return;
      }
      this.fetchCurrentUser().subscribe({
        next: (user) => {
          this.setCurrentUser(user);
          resolve();
        },
        error: () => {
          this.handleAuthSuccess(null); // handle logout();
          resolve();
        },
      });
    });
  }
  private fetchCurrentUser() {
    return this.http.get(this.apiUrl + '/current-user');
  }

  logout() {
    localStorage.removeItem('token');
    this.currentUserSubject.next(null);
    this.router.navigate(['/']);
  }
}
