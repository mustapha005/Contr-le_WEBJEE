import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './admin.component.html',
  styleUrl: './admin.component.scss'
})
export class AdminComponent {
  roles = [
    { role: 'ROLE_CLIENT', permissions: 'Consulter les vehicules disponibles et creer une location.' },
    { role: 'ROLE_EMPLOYE', permissions: 'Gerer agences, vehicules et cycle de vie des locations.' },
    { role: 'ROLE_ADMIN', permissions: 'Toutes les permissions, y compris les suppressions.' }
  ];
}
