import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import {
  Agency,
  AgencyRequest,
  DashboardStats,
  Rental,
  RentalRequest,
  Vehicle,
  VehicleRequest,
  VehicleStatus,
  VehicleType
} from './models';

export interface VehicleFilter {
  status?: VehicleStatus | '';
  type?: VehicleType | '';
  agencyId?: number | '';
  keyword?: string;
}

@Injectable({ providedIn: 'root' })
export class ApiService {
  private readonly http = inject(HttpClient);

  getStats(): Observable<DashboardStats> {
    return this.http.get<DashboardStats>('/api/dashboard/stats');
  }

  getAgencies(): Observable<Agency[]> {
    return this.http.get<Agency[]>('/api/agencies');
  }

  createAgency(payload: AgencyRequest): Observable<Agency> {
    return this.http.post<Agency>('/api/agencies', payload);
  }

  updateAgency(id: number, payload: AgencyRequest): Observable<Agency> {
    return this.http.put<Agency>(`/api/agencies/${id}`, payload);
  }

  deleteAgency(id: number): Observable<void> {
    return this.http.delete<void>(`/api/agencies/${id}`);
  }

  getVehicles(filter: VehicleFilter = {}): Observable<Vehicle[]> {
    let params = new HttpParams();
    if (filter.status) params = params.set('status', filter.status);
    if (filter.type) params = params.set('type', filter.type);
    if (filter.agencyId) params = params.set('agencyId', filter.agencyId);
    if (filter.keyword) params = params.set('keyword', filter.keyword);
    return this.http.get<Vehicle[]>('/api/vehicles', { params });
  }

  createVehicle(payload: VehicleRequest): Observable<Vehicle> {
    return this.http.post<Vehicle>('/api/vehicles', payload);
  }

  updateVehicle(id: number, payload: VehicleRequest): Observable<Vehicle> {
    return this.http.put<Vehicle>(`/api/vehicles/${id}`, payload);
  }

  deleteVehicle(id: number): Observable<void> {
    return this.http.delete<void>(`/api/vehicles/${id}`);
  }

  getRentals(): Observable<Rental[]> {
    return this.http.get<Rental[]>('/api/rentals');
  }

  createRental(payload: RentalRequest): Observable<Rental> {
    return this.http.post<Rental>('/api/rentals', payload);
  }

  finishRental(id: number): Observable<Rental> {
    return this.http.patch<Rental>(`/api/rentals/${id}/finish`, {});
  }

  cancelRental(id: number): Observable<Rental> {
    return this.http.patch<Rental>(`/api/rentals/${id}/cancel`, {});
  }
}
