export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  msg: string;
  timestamp: string;
  userName: string;
  userId: number;
  roles: string[];
}

export interface SignupRequest {
  username: string;
  password: string;
  patientName: string;
  phoneNo: string;
  address: string;
  ssn: number;
  insuranceId: number;
}

export interface Patient {
  ssn: number;
  name: string;
  address: string;
  phone: string;
  insuranceId: number;
  physicianId?: number | null;
}

export interface Physician {
  employeeId?: number;
  name: string;
  position: string;
  ssn: number;
}

export interface Nurse {
  employeeId?: number;
  name: string;
  position: string;
  registered: boolean;
  ssn: number;
}

export interface Appointment {
  appointmentID?: number;
  patientId: number;
  physicianId: number;
  nurseId?: number | null;
  starto: string;
  endo: string;
  examinationRoom: string;
}

export interface Medication {
  code?: number;
  name: string;
  brand: string;
  description?: string;
}

export interface Procedure {
  code?: number;
  name: string;
  cost: number;
}

export interface Room {
  roomNumber: number;
  roomType: string;
  unavailable: boolean;
  blockFloor: number;
  blockCode: number;
}

export interface Block {
  blockFloor: number;
  blockCode: number;
}

export interface Stay {
  stayId?: number;
  patientSsn: number;
  patientName?: string;
  roomNumber: number;
  roomType?: string;
  stayStart: string;
  stayEnd?: string | null;
}

export interface Prescribes {
  physicianId: number;
  patientSsn: number;
  medicationId: number;
  date: string;
  dose: string;
  appointmentId?: number | null;
}

export interface Undergoes {
  patientId: number;
  procedureCode: number;
  stayId: number;
  dateUndergoes: string;
  physicianId: number;
  assistingNurseId?: number | null;
}

export interface OnCall {
  nurseId: number;
  blockFloor: number;
  blockCode: number;
  onCallStart: string;
  onCallEnd: string;
}

export interface Department {
  departmentId?: number;
  name: string;
  headId: number;
  headName?: string;
}

export interface Affiliation {
  physicianId: number;
  physicianName?: string;
  departmentId: number;
  departmentName?: string;
  primaryAffiliation: boolean;
}

export interface TrainedIn {
  physicianId: number;
  treatmentId: number;
  certificationDate: string;
  certificationExpires: string;
}

export interface PatientDashboard {
  patient: Patient;
  appointments: Appointment[];
  stay: Stay | null;
  medications: Medication[];
}

export interface ApiError {
  errMsg?: string;
  status?: string;
  errMap?: Record<string, string[]>;
  message?: string;
}
