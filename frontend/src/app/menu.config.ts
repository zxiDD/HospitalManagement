export interface MenuItem {
  label: string;
  route: string;
  roles: string[];
  icon: string;
  section?: string;
}

export const menuItems: MenuItem[] = [
  // Overview
  { label: 'Hospital Overview', route: '/dashboard', roles: ['ROLE_ADMIN'], icon: 'dashboard', section: 'Main' },
  { label: 'Patient Portal', route: '/patients/:userId/dashboard', roles: ['ROLE_PATIENT'], icon: 'patient-dash', section: 'Main' },
  { label: 'Book Appointment', route: '/patient/book', roles: ['ROLE_PATIENT'], icon: 'appointments', section: 'Quick Links' },

  // Patient Care
  { label: 'Patient Registry', route: '/patients', roles: ['ROLE_ADMIN'], icon: 'patients', section: 'Patient Care' },
  { label: 'Doctor Directory', route: '/physicians', roles: ['ROLE_ADMIN'], icon: 'physicians', section: 'Patient Care' },
  { label: 'Nursing Staff', route: '/nurses', roles: ['ROLE_ADMIN'], icon: 'nurses', section: 'Patient Care' },
  { label: 'Appointments', route: '/appointments', roles: ['ROLE_ADMIN', 'ROLE_PATIENT'], icon: 'appointments', section: 'Patient Care' },

  // Services
  { label: 'Medications', route: '/medications', roles: ['ROLE_ADMIN'], icon: 'medications', section: 'Medical Services' },
  { label: 'Prescriptions', route: '/prescribes', roles: ['ROLE_ADMIN', 'ROLE_PATIENT'], icon: 'prescriptions', section: 'Medical Services' },
  { label: 'Procedure List', route: '/procedures', roles: ['ROLE_ADMIN'], icon: 'procedures', section: 'Medical Services' },

  // Facility
  { label: 'Room Status', route: '/room', roles: ['ROLE_ADMIN'], icon: 'rooms', section: 'Facility Management' },
  { label: 'Building Blocks', route: '/blocks', roles: ['ROLE_ADMIN'], icon: 'blocks', section: 'Facility Management' },
  { label: 'Inpatient Records', route: '/stays', roles: ['ROLE_ADMIN'], icon: 'stays', section: 'Facility Management' },

  // Staffing & Operations
  { label: 'Procedure Records', route: '/undergoes', roles: ['ROLE_ADMIN'], icon: 'undergoes', section: 'Staffing & Operations' },
  { label: 'Nurse Schedules', route: '/oncall', roles: ['ROLE_ADMIN'], icon: 'oncall', section: 'Staffing & Operations' },
  { label: 'Departments', route: '/departments', roles: ['ROLE_ADMIN'], icon: 'departments', section: 'Staffing & Operations' },
  { label: 'Staff Placement', route: '/affiliations', roles: ['ROLE_ADMIN'], icon: 'affiliations', section: 'Staffing & Operations' },
  { label: 'Certifications', route: '/trainedin', roles: ['ROLE_ADMIN'], icon: 'training', section: 'Staffing & Operations' },
];
