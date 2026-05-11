import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ApiService } from '../../core/api.service';
import { AuthService } from '../../core/auth.service';
import { DashboardStats, Vehicle } from '../../core/models';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit {
  readonly auth = inject(AuthService);
  private readonly api = inject(ApiService);

  stats?: DashboardStats;
  availableVehicles: Vehicle[] = [];
  loading = true;

  ngOnInit(): void {
    if (this.auth.canManage()) {
      this.api.getStats().subscribe(stats => this.stats = stats);
    }
    this.api.getVehicles({ status: 'DISPONIBLE' }).subscribe({
      next: vehicles => {
        this.availableVehicles = vehicles.slice(0, 6);
        this.loading = false;
      },
      error: () => this.loading = false
    });
  }

  statusClass(status: string): string {
    return status === 'DISPONIBLE' ? 'disponible' : status === 'LOUE' ? 'loue' : 'maintenance';
  }
}
