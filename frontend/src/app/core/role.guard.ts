import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from './auth.service';
import { UserRole } from './models';

export const roleGuard: CanActivateFn = route => {
  const roles = route.data['roles'] as UserRole[] | undefined;
  const auth = inject(AuthService);
  const router = inject(Router);
  if (!roles || roles.some(role => auth.hasRole(role))) {
    return true;
  }
  return router.createUrlTree(['/dashboard']);
};
