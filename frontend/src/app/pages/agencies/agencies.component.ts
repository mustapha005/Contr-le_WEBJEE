import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ApiService } from '../../core/api.service';
import { AuthService } from '../../core/auth.service';
import { Agency } from '../../core/models';

@Component({
  selector: 'app-agencies',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './agencies.component.html',
  styleUrl: './agencies.component.scss'
})
export class AgenciesComponent implements OnInit {
  readonly auth = inject(AuthService);
  private readonly api = inject(ApiService);
  private readonly fb = inject(FormBuilder);

  agencies: Agency[] = [];
  editingId?: number;
  loading = false;
  saving = false;
  error = '';
  success = '';

  form = this.fb.nonNullable.group({
    nom: ['', Validators.required],
    adresse: ['', Validators.required],
    ville: ['', Validators.required],
    telephone: ['', Validators.required]
  });

  ngOnInit(): void {
    this.loadAgencies();
  }

  loadAgencies(): void {
    this.loading = true;
    this.api.getAgencies().subscribe({
      next: agencies => {
        this.agencies = agencies;
        this.loading = false;
      },
      error: err => {
        this.error = err?.error?.message || 'Erreur de chargement des agences.';
        this.loading = false;
      }
    });
  }

  save(): void {
    if (!this.auth.canManage()) return;
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.saving = true;
    this.error = '';
    this.success = '';
    const raw = this.form.getRawValue();
    const request$ = this.editingId ? this.api.updateAgency(this.editingId, raw) : this.api.createAgency(raw);
    request$.subscribe({
      next: () => {
        this.success = this.editingId ? 'Agence modifiee.' : 'Agence ajoutee.';
        this.saving = false;
        this.cancelEdit();
        this.loadAgencies();
      },
      error: err => {
        this.error = err?.error?.message || 'Operation impossible.';
        this.saving = false;
      }
    });
  }

  edit(agency: Agency): void {
    this.editingId = agency.id;
    this.form.setValue({
      nom: agency.nom,
      adresse: agency.adresse,
      ville: agency.ville,
      telephone: agency.telephone
    });
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  delete(agency: Agency): void {
    if (!this.auth.isAdmin()) return;
    if (!confirm(`Supprimer l'agence ${agency.nom} ?`)) return;
    this.api.deleteAgency(agency.id).subscribe({
      next: () => this.loadAgencies(),
      error: err => this.error = err?.error?.message || 'Suppression impossible.'
    });
  }

  cancelEdit(): void {
    this.editingId = undefined;
    this.form.reset({ nom: '', adresse: '', ville: '', telephone: '' });
  }
}
