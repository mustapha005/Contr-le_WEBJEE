export type UserRole = 'ROLE_CLIENT' | 'ROLE_EMPLOYE' | 'ROLE_ADMIN';
export type VehicleStatus = 'DISPONIBLE' | 'LOUE' | 'EN_MAINTENANCE';
export type VehicleType = 'VOITURE' | 'MOTO';
export type FuelType = 'ESSENCE' | 'DIESEL' | 'HYBRIDE' | 'ELECTRIQUE';
export type GearBox = 'MANUELLE' | 'AUTOMATIQUE';
export type MotorcycleType = 'SPORTIVE' | 'SCOOTER' | 'ROADSTER' | 'TOURING';
export type RentalStatus = 'ACTIVE' | 'TERMINEE' | 'ANNULEE';

export interface AuthRequest {
  username: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  password: string;
  fullName: string;
}

export interface AuthResponse {
  token: string;
  tokenType: string;
  username: string;
  fullName: string;
  role: UserRole;
}

export interface Agency {
  id: number;
  nom: string;
  adresse: string;
  ville: string;
  telephone: string;
  vehicleCount: number;
}

export interface AgencyRequest {
  nom: string;
  adresse: string;
  ville: string;
  telephone: string;
}

export interface Vehicle {
  id: number;
  marque: string;
  modele: string;
  matricule: string;
  prixParJour: number;
  dateMiseEnService: string;
  statut: VehicleStatus;
  agencyId: number;
  agencyName: string;
  type: VehicleType;
  nombrePortes?: number;
  typeCarburant?: FuelType;
  boiteVitesse?: GearBox;
  cylindree?: number;
  typeMoto?: MotorcycleType;
  casqueInclus?: boolean;
}

export interface VehicleRequest {
  marque: string;
  modele: string;
  matricule: string;
  prixParJour: number;
  dateMiseEnService: string;
  statut: VehicleStatus;
  agencyId: number;
  type: VehicleType;
  nombrePortes?: number | null;
  typeCarburant?: FuelType | null;
  boiteVitesse?: GearBox | null;
  cylindree?: number | null;
  typeMoto?: MotorcycleType | null;
  casqueInclus?: boolean | null;
}

export interface Rental {
  id: number;
  vehicleId: number;
  vehicleLabel: string;
  matricule: string;
  vehicleType: VehicleType;
  agencyName: string;
  clientName: string;
  clientEmail: string;
  dateDebut: string;
  dateFin: string;
  totalPrice: number;
  status: RentalStatus;
  createdAt: string;
}

export interface RentalRequest {
  vehicleId: number;
  clientName: string;
  clientEmail: string;
  dateDebut: string;
  dateFin: string;
}

export interface DashboardStats {
  totalAgencies: number;
  totalVehicles: number;
  availableVehicles: number;
  rentedVehicles: number;
  maintenanceVehicles: number;
  totalRentals: number;
  activeRentals: number;
  cars: number;
  motorcycles: number;
}
