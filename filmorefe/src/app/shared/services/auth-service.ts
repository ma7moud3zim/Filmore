import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiUrl = environment.apiUrl + '/auth';

  constructor(private http: HttpClient) {}

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
    return this.http.get(this.apiUrl + 'verify-email?token=' + token);
  }
}
