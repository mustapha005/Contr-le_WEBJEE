import { Routes } from '@angular/router';
import { authGuard } from './core/auth.guard';
import { roleGuard } from './core/role.guard';

export const routes: Routes = [
  {
    path: 'login',
    loadComponent: () => import('./pages/login/login.component').then(m => m.LoginComponent)
  },
  {
    path: 'dashboard',
    canActivate: [authGuard],
    loadComponent: () => import('./pages/dashboard/dashboard.component').then(m => m.DashboardComponent)
  },
  {
    path: 'vehicles',
    canActivate: [authGuard],
    loadComponent: () => import('./pages/vehicles/vehicles.component').then(m => m.VehiclesComponent)
  },
  {
    path: 'agencies',
    canActivate: [authGuard],
    loadComponent: () => import('./pages/agencies/agencies.component').then(m => m.AgenciesComponent)
  },
  {
    path: 'rentals',
    canActivate: [authGuard],
    loadComponent: () => import('./pages/rentals/rentals.component').then(m => m.RentalsComponent)
  },
  {
    path: 'admin',
    canActivate: [authGuard, roleGuard],
    data: { roles: ['ROLE_ADMIN'] },
    loadComponent: () => import('./pages/admin/admin.component').then(m => m.AdminComponent)
  },
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
  { path: '**', redirectTo: 'dashboard' }
];
