import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ApiService } from '../../core/api.service';
import { AuthService } from '../../core/auth.service';
import { Rental, Vehicle } from '../../core/models';

@Component({
  selector: 'app-rentals',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './rentals.component.html',
  styleUrl: './rentals.component.scss'
})
export class RentalsComponent implements OnInit {
  readonly auth = inject(AuthService);
  private readonly api = inject(ApiService);
  private readonly fb = inject(FormBuilder);

  rentals: Rental[] = [];
  availableVehicles: Vehicle[] = [];
  error = '';
  success = '';
  saving = false;

  form = this.fb.nonNullable.group({
    vehicleId: [0, Validators.min(1)],
    clientName: ['', Validators.required],
    clientEmail: ['', [Validators.required, Validators.email]],
    dateDebut: [new Date().toISOString().slice(0, 10), Validators.required],
    dateFin: [new Date(Date.now() + 86400000).toISOString().slice(0, 10), Validators.required]
  });

  ngOnInit(): void {
    this.loadVehicles();
    this.loadRentals();
    if (this.auth.currentUser && !this.form.controls.clientName.value) {
      this.form.patchValue({ clientName: this.auth.currentUser.fullName });
    }
  }

  loadVehicles(): void {
    this.api.getVehicles({ status: 'DISPONIBLE' }).subscribe(vehicles => {
      this.availableVehicles = vehicles;
      if (vehicles.length && this.form.controls.vehicleId.value === 0) {
        this.form.controls.vehicleId.setValue(vehicles[0].id);
      }
    });
  }

  loadRentals(): void {
    if (!this.auth.canManage()) return;
    this.api.getRentals().subscribe({
      next: rentals => this.rentals = rentals,
      error: err => this.error = err?.error?.message || 'Erreur de chargement des locations.'
    });
  }

  createRental(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.saving = true;
    this.error = '';
    this.success = '';
    const raw = this.form.getRawValue();
    this.api.createRental({
      vehicleId: Number(raw.vehicleId),
      clientName: raw.clientName,
      clientEmail: raw.clientEmail,
      dateDebut: raw.dateDebut,
      dateFin: raw.dateFin
    }).subscribe({
      next: rental => {
        this.success = `Location #${rental.id} creee. Total: ${rental.totalPrice} DH.`;
        this.saving = false;
        this.loadVehicles();
        this.loadRentals();
      },
      error: err => {
        this.error = err?.error?.message || 'Creation impossible.';
        this.saving = false;
      }
    });
  }

  finish(rental: Rental): void {
    this.api.finishRental(rental.id).subscribe(() => {
      this.loadRentals();
      this.loadVehicles();
    });
  }

  cancel(rental: Rental): void {
    this.api.cancelRental(rental.id).subscribe(() => {
      this.loadRentals();
      this.loadVehicles();
    });
  }

  statusClass(status: string): string {
    return status === 'ACTIVE' ? 'loue' : status === 'TERMINEE' ? 'disponible' : 'maintenance';
  }
}
