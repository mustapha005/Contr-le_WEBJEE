import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ApiService } from '../../core/api.service';
import { AuthService } from '../../core/auth.service';
import { Agency, FuelType, GearBox, MotorcycleType, Vehicle, VehicleRequest, VehicleStatus, VehicleType } from '../../core/models';

@Component({
  selector: 'app-vehicles',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './vehicles.component.html',
  styleUrl: './vehicles.component.scss'
})
export class VehiclesComponent implements OnInit {
  readonly auth = inject(AuthService);
  private readonly api = inject(ApiService);
  private readonly fb = inject(FormBuilder);

  vehicles: Vehicle[] = [];
  agencies: Agency[] = [];
  loading = false;
  saving = false;
  error = '';
  success = '';
  editingId?: number;

  statuses: VehicleStatus[] = ['DISPONIBLE', 'LOUE', 'EN_MAINTENANCE'];
  types: VehicleType[] = ['VOITURE', 'MOTO'];
  fuelTypes: FuelType[] = ['ESSENCE', 'DIESEL', 'HYBRIDE', 'ELECTRIQUE'];
  gearBoxes: GearBox[] = ['MANUELLE', 'AUTOMATIQUE'];
  motorcycleTypes: MotorcycleType[] = ['SPORTIVE', 'SCOOTER', 'ROADSTER', 'TOURING'];

  filterForm = this.fb.nonNullable.group({
    status: ['' as VehicleStatus | ''],
    type: ['' as VehicleType | ''],
    agencyId: [0],
    keyword: ['']
  });

  vehicleForm = this.fb.nonNullable.group({
    type: ['VOITURE' as VehicleType, Validators.required],
    marque: ['', Validators.required],
    modele: ['', Validators.required],
    matricule: ['', Validators.required],
    prixParJour: [300, [Validators.required, Validators.min(1)]],
    dateMiseEnService: [new Date().toISOString().slice(0, 10), Validators.required],
    statut: ['DISPONIBLE' as VehicleStatus, Validators.required],
    agencyId: [0, Validators.min(1)],
    nombrePortes: [5],
    typeCarburant: ['ESSENCE' as FuelType],
    boiteVitesse: ['MANUELLE' as GearBox],
    cylindree: [125],
    typeMoto: ['SCOOTER' as MotorcycleType],
    casqueInclus: [true]
  });

  ngOnInit(): void {
    this.loadAgencies();
    this.loadVehicles();
  }

  get selectedType(): VehicleType {
    return this.vehicleForm.controls.type.value;
  }

  loadAgencies(): void {
    this.api.getAgencies().subscribe(agencies => {
      this.agencies = agencies;
      if (agencies.length && this.vehicleForm.controls.agencyId.value === 0) {
        this.vehicleForm.controls.agencyId.setValue(agencies[0].id);
      }
    });
  }

  loadVehicles(): void {
    this.loading = true;
    const raw = this.filterForm.getRawValue();
    this.api.getVehicles({
      status: raw.status,
      type: raw.type,
      agencyId: raw.agencyId > 0 ? raw.agencyId : '',
      keyword: raw.keyword
    }).subscribe({
      next: vehicles => {
        this.vehicles = vehicles;
        this.loading = false;
      },
      error: err => {
        this.error = err?.error?.message || 'Erreur de chargement des vehicules.';
        this.loading = false;
      }
    });
  }

  resetFilters(): void {
    this.filterForm.setValue({ status: '', type: '', agencyId: 0, keyword: '' });
    this.loadVehicles();
  }

  saveVehicle(): void {
    if (!this.auth.canManage()) return;
    if (this.vehicleForm.invalid) {
      this.vehicleForm.markAllAsTouched();
      return;
    }
    this.saving = true;
    this.error = '';
    this.success = '';
    const payload = this.toVehiclePayload();
    const request$ = this.editingId ? this.api.updateVehicle(this.editingId, payload) : this.api.createVehicle(payload);
    request$.subscribe({
      next: () => {
        this.success = this.editingId ? 'Vehicule modifie avec succes.' : 'Vehicule ajoute avec succes.';
        this.saving = false;
        this.cancelEdit();
        this.loadVehicles();
      },
      error: err => {
        this.error = err?.error?.message || 'Operation impossible.';
        this.saving = false;
      }
    });
  }

  edit(vehicle: Vehicle): void {
    this.editingId = vehicle.id;
    this.vehicleForm.patchValue({
      type: vehicle.type,
      marque: vehicle.marque,
      modele: vehicle.modele,
      matricule: vehicle.matricule,
      prixParJour: Number(vehicle.prixParJour),
      dateMiseEnService: vehicle.dateMiseEnService,
      statut: vehicle.statut,
      agencyId: vehicle.agencyId,
      nombrePortes: vehicle.nombrePortes ?? 5,
      typeCarburant: vehicle.typeCarburant ?? 'ESSENCE',
      boiteVitesse: vehicle.boiteVitesse ?? 'MANUELLE',
      cylindree: vehicle.cylindree ?? 125,
      typeMoto: vehicle.typeMoto ?? 'SCOOTER',
      casqueInclus: vehicle.casqueInclus ?? true
    });
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  delete(vehicle: Vehicle): void {
    if (!this.auth.isAdmin()) return;
    if (!confirm(`Supprimer ${vehicle.marque} ${vehicle.modele} ?`)) return;
    this.api.deleteVehicle(vehicle.id).subscribe({
      next: () => this.loadVehicles(),
      error: err => this.error = err?.error?.message || 'Suppression impossible.'
    });
  }

  cancelEdit(): void {
    this.editingId = undefined;
    this.vehicleForm.reset({
      type: 'VOITURE',
      marque: '',
      modele: '',
      matricule: '',
      prixParJour: 300,
      dateMiseEnService: new Date().toISOString().slice(0, 10),
      statut: 'DISPONIBLE',
      agencyId: this.agencies[0]?.id ?? 0,
      nombrePortes: 5,
      typeCarburant: 'ESSENCE',
      boiteVitesse: 'MANUELLE',
      cylindree: 125,
      typeMoto: 'SCOOTER',
      casqueInclus: true
    });
  }

  statusClass(status: VehicleStatus): string {
    return status === 'DISPONIBLE' ? 'disponible' : status === 'LOUE' ? 'loue' : 'maintenance';
  }

  private toVehiclePayload(): VehicleRequest {
    const raw = this.vehicleForm.getRawValue();
    const base: VehicleRequest = {
      marque: raw.marque,
      modele: raw.modele,
      matricule: raw.matricule,
      prixParJour: Number(raw.prixParJour),
      dateMiseEnService: raw.dateMiseEnService,
      statut: raw.statut,
      agencyId: Number(raw.agencyId),
      type: raw.type,
      nombrePortes: null,
      typeCarburant: null,
      boiteVitesse: null,
      cylindree: null,
      typeMoto: null,
      casqueInclus: null
    };
    if (raw.type === 'VOITURE') {
      base.nombrePortes = Number(raw.nombrePortes);
      base.typeCarburant = raw.typeCarburant;
      base.boiteVitesse = raw.boiteVitesse;
    } else {
      base.cylindree = Number(raw.cylindree);
      base.typeMoto = raw.typeMoto;
      base.casqueInclus = Boolean(raw.casqueInclus);
    }
    return base;
  }
}
