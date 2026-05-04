import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import { AuthService } from '../services/auth.service';
import { ApiError } from '../models/api.models';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const auth = inject(AuthService);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401) {
        auth.logout();
      }

      return throwError(() => normaliseError(error));
    }),
  );
};

function normaliseError(error: HttpErrorResponse): Error {
  const body = error.error as ApiError | string | null;

  if (typeof body === 'string') {
    return new Error(body || error.message);
  }

  if (body?.errMap) {
    const first = Object.values(body.errMap).flat()[0];
    return new Error(first ?? body.errMsg ?? error.message);
  }

  return new Error(body?.errMsg ?? body?.message ?? error.message);
}
