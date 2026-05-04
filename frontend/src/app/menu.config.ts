export const menuItems: any = {
  ROLE_ADMIN: [
    {
      label: 'Dashboard',
      icon: 'bi-speedometer2',
      route: '/dashboard',
      exact: true,
    },

    { section: 'MANAGEMENT' },
    { label: 'Patients', icon: 'bi-people', route: '/patients' },
    { label: 'Physicians', icon: 'bi-person-badge', route: '/doctors' },
    { label: 'Appointments', icon: 'bi-calendar', route: '/appointments' },
    { label: 'Rooms', icon: 'bi-door-open', route: '/rooms' },
    { label: 'Medications', icon: 'bi-capsule', route: '/medications' },

    { section: 'REPORTS' },
    {
      label: 'Appointments Report',
      icon: 'bi-bar-chart',
      route: '/appointment-report',
    },
    {
      label: 'Patients Report',
      icon: 'bi-file-earmark-text',
      route: '/patient-report',
    },
  ],

  ROLE_PATIENT: [
    {
      label: 'Dashboard',
      icon: 'bi-house',
      route: '/patient/dashboard',
      exact: true,
    },
    {
      label: 'Appointments',
      icon: 'bi-calendar',
      route: '/patient/appointments',
    },
    { label: 'My Stay', icon: 'bi-hospital', route: '/patient/stay' },
    { label: 'Medications', icon: 'bi-capsule', route: '/patient/medications' },
    { label: 'Profile', icon: 'bi-person', route: '/patient/profile' },
  ],
};
