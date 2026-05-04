import { DatePipe, NgFor, NgIf, TitleCasePipe } from '@angular/common';
import { Component, OnDestroy, OnInit, signal } from '@angular/core';
import { FormsModule, ReactiveFormsModule, UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { AuthService } from '../../core/services/auth.service';
import { ApiService } from '../../core/services/api.service';
import { ToastService } from '../../core/services/toast.service';

type FieldType = 'text' | 'number' | 'boolean' | 'datetime' | 'date';

interface FieldConfig {
  key: string;
  label: string;
  type: FieldType;
  required?: boolean;
}

interface FilterConfig {
  label: string;
  inputs: FieldConfig[];
  path: (values: Record<string, unknown>, auth: AuthService) => string;
  params?: (values: Record<string, unknown>) => Record<string, string | number | boolean | null | undefined>;
}

interface RowAction {
  label: string;
  adminOnly?: boolean;
  run: (row: Record<string, unknown>, api: ApiService, toast: ToastService, reload: () => void) => void;
}

interface EntityConfig {
  title: string;
  description: string;
  roles: string[];
  listPath: string | ((auth: AuthService) => string);
  createPath?: string;
  updatePath?: (row: Record<string, unknown>) => string;
  deletePath?: (row: Record<string, unknown>) => string;
  deleteParams?: (row: Record<string, unknown>) => Record<string, string | number | boolean>;
  columns: FieldConfig[];
  createFields?: FieldConfig[];
  filters?: FilterConfig[];
  rowActions?: RowAction[];
}

const adminOnly = ['ROLE_ADMIN'];
const shared = ['ROLE_ADMIN', 'ROLE_PATIENT'];

const configs: Record<string, EntityConfig> = {
  patients: {
    title: 'Patient Records',
    description: 'Registry of all patients including personal details, insurance coverage, and primary care assignments.',
    roles: adminOnly,
    listPath: '/patients',
    createPath: '/admin/patients',
    updatePath: (row) => `/admin/patients/${row['ssn']}`,
    deletePath: (row) => `/admin/patients/${row['ssn']}`,
    columns: [
      { key: 'ssn', label: 'ID (SSN)', type: 'number' },
      { key: 'name', label: 'Patient Name', type: 'text' },
      { key: 'phone', label: 'Contact Phone', type: 'text' },
      { key: 'address', label: 'Home Address', type: 'text' },
      { key: 'insuranceId', label: 'Insurance Policy', type: 'number' },
      { key: 'physicianName', label: 'Primary Care Doctor', type: 'text' },
    ],
    createFields: [
      { key: 'ssn', label: 'Identity Number (SSN)', type: 'number', required: true },
      { key: 'name', label: 'Full Name', type: 'text', required: true },
      { key: 'phone', label: 'Phone Number', type: 'text', required: true },
      { key: 'address', label: 'Residential Address', type: 'text', required: true },
      { key: 'insuranceId', label: 'Insurance Policy Number', type: 'number', required: true },
      { key: 'physicianId', label: 'Assigned Physician ID', type: 'number', required: true },
    ],
    filters: [
      {
        label: 'Find by Patient Name',
        inputs: [{ key: 'name', label: 'Full Name', type: 'text', required: true }],
        path: (values) => `/patients/name/${values['name']}`,
      },
      {
        label: 'Advanced Search',
        inputs: [
          { key: 'name', label: 'Patient Name', type: 'text', required: true },
          { key: 'address', label: 'Residential Area', type: 'text', required: true },
        ],
        path: () => '/patients/search',
        params: (values) => ({ name: String(values['name']), address: String(values['address']) }),
      },
    ],
  },
  physicians: {
    title: 'Medical Doctors',
    description: 'Clinical staff directory and physician management system.',
    roles: shared,
    listPath: '/physicians',
    createPath: '/admin/physicians',
    updatePath: (row) => `/admin/physicians/${row['employeeId']}`,
    deletePath: (row) => `/admin/physicians/${row['employeeId']}`,
    columns: [
      { key: 'employeeId', label: 'Staff ID', type: 'number' },
      { key: 'name', label: 'Doctor Name', type: 'text' },
      { key: 'position', label: 'Specialty/Position', type: 'text' },
      { key: 'ssn', label: 'Identity ID', type: 'number' },
    ],
    createFields: [
      { key: 'name', label: 'Physician Full Name', type: 'text', required: true },
      { key: 'position', label: 'Medical Specialty', type: 'text', required: true },
      { key: 'ssn', label: 'Identity Number (SSN)', type: 'number', required: true },
    ],
    filters: [
      {
        label: 'Filter by Specialty',
        inputs: [{ key: 'position', label: 'Medical Specialty', type: 'text', required: true }],
        path: (values) => `/physicians/position/${values['position']}`,
      },
    ],
  },
  nurses: {
    title: 'Nursing Staff',
    description: 'Registry of nursing personnel and professional registration status.',
    roles: adminOnly,
    listPath: '/nurses',
    createPath: '/admin/nurses',
    updatePath: (row) => `/admin/nurses/${row['employeeId']}`,
    deletePath: (row) => `/admin/nurses/${row['employeeId']}`,
    columns: [
      { key: 'employeeId', label: 'Staff ID', type: 'number' },
      { key: 'name', label: 'Nurse Name', type: 'text' },
      { key: 'position', label: 'Role/Position', type: 'text' },
      { key: 'registered', label: 'RN Registered?', type: 'boolean' },
      { key: 'ssn', label: 'Identity ID', type: 'number' },
    ],
    createFields: [
      { key: 'name', label: 'Full Name', type: 'text', required: true },
      { key: 'position', label: 'Assigned Role', type: 'text', required: true },
      { key: 'registered', label: 'Is Registered Nurse?', type: 'boolean' },
      { key: 'ssn', label: 'Identity Number (SSN)', type: 'number', required: true },
    ],
  },
  appointments: {
    title: 'Appointments',
    description: 'Book and review consultations across patients, physicians, nurses, and rooms.',
    roles: shared,
    listPath: (auth) => auth.isPatient() && auth.userId() ? `/appointments/patients/${auth.userId()}/appointments` : '/appointments',
    createPath: '/appointments',
    columns: [
      { key: 'appointmentID', label: 'ID', type: 'number' },
      { key: 'patientName', label: 'Patient Name', type: 'text' },
      { key: 'physicianName', label: 'Physician Name', type: 'text' },
      { key: 'nurseName', label: 'Nurse Name', type: 'text' },
      { key: 'starto', label: 'Start', type: 'datetime' },
      { key: 'endo', label: 'End', type: 'datetime' },
      { key: 'examinationRoom', label: 'Room', type: 'text' },
    ],
    createFields: [
      { key: 'patientId', label: 'Patient Identity (SSN)', type: 'number', required: true },
      { key: 'physicianId', label: 'Attending Doctor ID', type: 'number', required: true },
      { key: 'nurseId', label: 'Preparing Nurse ID', type: 'number' },
      { key: 'starto', label: 'Appointment Start', type: 'datetime', required: true },
      { key: 'endo', label: 'Appointment End', type: 'datetime', required: true },
      { key: 'examinationRoom', label: 'Medical Room', type: 'text', required: true },
    ],
    rowActions: [
      {
        label: 'Assign',
        adminOnly: true,
        run: (row, api, toast, reload) => {
          const room = prompt('Enter Examination Room (e.g. 101):', String(row['examinationRoom'] || ''));
          if (room === null) return; 
          const nurseId = prompt('Enter Nurse ID (leave blank to skip):', String(row['nurseId'] || ''));
          if (nurseId === null) return;
          
          let path = `/admin/appointments/${row['appointmentID']}/assign?`;
          if (room.trim()) path += `room=${encodeURIComponent(room.trim())}&`;
          if (nurseId.trim() && !isNaN(Number(nurseId))) path += `nurseId=${Number(nurseId.trim())}`;
          
          api.patch(path).subscribe({
            next: () => { toast.success('Room / Nurse assigned successfully'); reload(); },
            error: (err: any) => { toast.error(err?.error?.message || 'Failed to assign. Nurse may not be on-call during this time.'); }
          });
        }
      }
    ],
    filters: [
      {
        label: 'By patient',
        inputs: [{ key: 'patientId', label: 'Patient SSN', type: 'number', required: true }],
        path: (values) => `/appointments/patients/${values['patientId']}/appointments`,
      },
      {
        label: 'By physician',
        inputs: [{ key: 'physicianId', label: 'Physician ID', type: 'number', required: true }],
        path: (values) => `/appointments/physician/${values['physicianId']}/appointments`,
      },
    ],
  },
  medications: {
    title: 'Medication Catalogue',
    description: 'Browse available medications, brands, and clinical descriptions.',
    roles: shared,
    listPath: '/medications',
    createPath: '/admin/medications',
    columns: [
      { key: 'code', label: 'Medication Ref', type: 'number' },
      { key: 'name', label: 'Generic Name', type: 'text' },
      { key: 'brand', label: 'Manufacturer/Brand', type: 'text' },
      { key: 'description', label: 'Clinical Description', type: 'text' },
    ],
    createFields: [
      { key: 'name', label: 'Medication Name', type: 'text', required: true },
      { key: 'brand', label: 'Manufacturer Name', type: 'text', required: true },
      { key: 'description', label: 'Brief Description', type: 'text' },
    ],
    filters: [
      { label: 'Search by Name', inputs: [{ key: 'name', label: 'Medication Name', type: 'text', required: true }], path: (v) => `/medications/name/${v['name']}` },
      { label: 'Search by Brand', inputs: [{ key: 'brand', label: 'Manufacturer Name', type: 'text', required: true }], path: (v) => `/medications/brand/${v['brand']}` },
    ],
  },
  prescribes: {
    title: 'Active Prescriptions',
    description: 'Track medication prescriptions linked to specific patients and appointments.',
    roles: shared,
    listPath: (auth) => auth.isPatient() && auth.userId() ? `/prescribes/patient/${auth.userId()}` : '/prescribes',
    createPath: '/admin/prescribes',
    columns: [
      { key: 'physicianName', label: 'Prescribing Doctor', type: 'text' },
      { key: 'patientName', label: 'Patient Name', type: 'text' },
      { key: 'medicationName', label: 'Medication', type: 'text' },
      { key: 'date', label: 'Date Issued', type: 'datetime' },
      { key: 'dose', label: 'Dosage Instructions', type: 'text' },
      { key: 'appointmentId', label: 'Appointment Ref', type: 'number' },
    ],
    createFields: [
      { key: 'physicianId', label: 'Prescribing Doctor ID', type: 'number', required: true },
      { key: 'patientSsn', label: 'Patient Identity (SSN)', type: 'number', required: true },
      { key: 'medicationId', label: 'Medication Ref Code', type: 'number', required: true },
      { key: 'date', label: 'Issue Date', type: 'datetime', required: true },
      { key: 'dose', label: 'Dosage (e.g. 500mg)', type: 'text', required: true },
      { key: 'appointmentId', label: 'Linked Appointment ID', type: 'number' },
    ],
    filters: [
      { label: 'Sort by Date Issued', inputs: [], path: () => '/prescribes/sorted' },
      { label: 'Filter by Patient', inputs: [{ key: 'ssn', label: 'Patient Identity (SSN)', type: 'number', required: true }], path: (v) => `/prescribes/patient/${v['ssn']}` },
    ],
  },
  procedures: {
    title: 'Procedures',
    description: 'Procedure catalogue with cost-based search.',
    roles: shared,
    listPath: '/procedures',
    createPath: '/admin/procedures',
    columns: [
      { key: 'code', label: 'Code', type: 'number' },
      { key: 'name', label: 'Name', type: 'text' },
      { key: 'cost', label: 'Cost', type: 'number' },
    ],
    createFields: [
      { key: 'name', label: 'Name', type: 'text', required: true },
      { key: 'cost', label: 'Cost', type: 'number', required: true },
    ],
    filters: [
      { label: 'By name', inputs: [{ key: 'name', label: 'Name', type: 'text', required: true }], path: (v) => `/procedures/name/${v['name']}` },
      { label: 'Cost less than', inputs: [{ key: 'cost', label: 'Cost', type: 'number', required: true }], path: () => '/procedures/cost/less-than', params: (v) => ({ cost: Number(v['cost']) }) },
      { label: 'Cost greater than', inputs: [{ key: 'cost', label: 'Cost', type: 'number', required: true }], path: () => '/procedures/cost/greater-than', params: (v) => ({ cost: Number(v['cost']) }) },
      {
        label: 'Cost range',
        inputs: [
          { key: 'min', label: 'Min', type: 'number', required: true },
          { key: 'max', label: 'Max', type: 'number', required: true },
        ],
        path: () => '/procedures/cost/range',
        params: (v) => ({ min: Number(v['min']), max: Number(v['max']) }),
      },
    ],
  },
  room: {
    title: 'Hospital Rooms',
    description: 'Facility room inventory, status tracking, and floor assignments.',
    roles: adminOnly,
    listPath: '/room',
    columns: [
      { key: 'roomNumber', label: 'Room #', type: 'number' },
      { key: 'roomType', label: 'Room Type', type: 'text' },
      { key: 'blockFloor', label: 'Floor', type: 'number' },
      { key: 'blockCode', label: 'Building Block', type: 'number' },
      { key: 'unavailable', label: 'Currently Occupied?', type: 'boolean' },
    ],
    rowActions: [
      {
        label: 'Update Status',
        adminOnly: true,
        run: (row, api, toast, reload) => {
          api.put(`/admin/room/${row['roomNumber']}/unavailable`, {}).subscribe({
            next: () => { toast.success('Room status updated successfully'); reload(); },
            error: (err: any) => { toast.error(err?.error?.message || 'Failed to update room status'); }
          });
        },
      },
    ],
    filters: [
      { label: 'Filter by Room Type', inputs: [{ key: 'roomType', label: 'Type (e.g. ICU)', type: 'text', required: true }], path: (v) => `/room/type/${v['roomType']}` },
      { label: 'Show Occupied Only', inputs: [{ key: 'unavailable', label: 'Is Occupied?', type: 'boolean' }], path: () => '/room/availability', params: (v) => ({ unavailable: Boolean(v['unavailable']) }) },
    ],
  },
  blocks: {
    title: 'Blocks',
    description: 'Building block lookup by floor and block code.',
    roles: adminOnly,
    listPath: '/blocks',
    columns: [
      { key: 'blockFloor', label: 'Floor', type: 'number' },
      { key: 'blockCode', label: 'Code', type: 'number' },
    ],
    filters: [
      {
        label: 'Lookup block',
        inputs: [
          { key: 'floor', label: 'Floor', type: 'number', required: true },
          { key: 'code', label: 'Code', type: 'number', required: true },
        ],
        path: () => '/blocks/id',
        params: (v) => ({ floor: Number(v['floor']), code: Number(v['code']) }),
      },
    ],
  },
  stays: {
    title: 'Inpatient Admissions',
    description: 'Active occupancy tracking and historical admission records.',
    roles: adminOnly,
    listPath: '/stays',
    createPath: '/admin/stays',
    columns: [
      { key: 'stayId', label: 'Admission ID', type: 'number' },
      { key: 'patientName', label: 'Patient Name', type: 'text' },
      { key: 'roomNumber', label: 'Room #', type: 'number' },
      { key: 'stayStart', label: 'Admission Date', type: 'datetime' },
      { key: 'stayEnd', label: 'Discharge Date', type: 'datetime' },
    ],
    createFields: [
      { key: 'patientSsn', label: 'Patient Identity (SSN)', type: 'number', required: true },
      { key: 'roomNumber', label: 'Room Number', type: 'number', required: true },
      { key: 'stayStart', label: 'Admission Date/Time', type: 'datetime', required: true },
      { key: 'stayEnd', label: 'Scheduled Discharge', type: 'datetime' },
    ],
    filters: [
      { label: 'Active Admissions', inputs: [], path: () => '/stays/active' },
      { label: 'Filter by Patient', inputs: [{ key: 'ssn', label: 'Patient Identity (SSN)', type: 'number', required: true }], path: (v) => `/stays/patient/${v['ssn']}` },
      { label: 'Filter by Room', inputs: [{ key: 'roomNumber', label: 'Room Number', type: 'number', required: true }], path: (v) => `/stays/room/${v['roomNumber']}` },
    ],
  },
  undergoes: {
    title: 'Procedure History',
    description: 'Comprehensive record of medical procedures performed on patients.',
    roles: adminOnly,
    listPath: '/undergoes',
    createPath: '/admin/undergoes',
    columns: [
      { key: 'patientName', label: 'Patient', type: 'text' },
      { key: 'procedureName', label: 'Medical Procedure', type: 'text' },
      { key: 'stayId', label: 'Admission ID', type: 'number' },
      { key: 'dateUndergoes', label: 'Date Performed', type: 'datetime' },
      { key: 'physicianName', label: 'Doctor', type: 'text' },
      { key: 'nurseName', label: 'Assisting Nurse', type: 'text' },
    ],
    createFields: [
      { key: 'patientId', label: 'Patient Identity (SSN)', type: 'number', required: true },
      { key: 'procedureCode', label: 'Procedure Code', type: 'number', required: true },
      { key: 'stayId', label: 'Admission Record ID', type: 'number', required: true },
      { key: 'dateUndergoes', label: 'Procedure Date', type: 'datetime', required: true },
      { key: 'physicianId', label: 'Doctor ID', type: 'number', required: true },
      { key: 'assistingNurseId', label: 'Assisting Nurse ID', type: 'number' },
    ],
  },
  oncall: {
    title: 'On-Call Schedules',
    description: 'Nursing on-call assignments and department block staffing.',
    roles: adminOnly,
    listPath: '/oncall',
    createPath: '/admin/oncall',
    deletePath: () => '/admin/oncall',
    deleteParams: (row) => ({
      nurseId: Number(row['nurseId']),
      blockFloor: Number(row['blockFloor']),
      blockCode: Number(row['blockCode']),
    }),
    columns: [
      { key: 'nurseName', label: 'Nurse Name', type: 'text' },
      { key: 'blockFloor', label: 'Floor Level', type: 'number' },
      { key: 'blockCode', label: 'Building Block', type: 'number' },
      { key: 'onCallStart', label: 'Shift Start', type: 'datetime' },
      { key: 'onCallEnd', label: 'Shift End', type: 'datetime' },
    ],
    createFields: [
      { key: 'nurseId', label: 'Nurse Staff ID', type: 'number', required: true },
      { key: 'blockFloor', label: 'Floor Number', type: 'number', required: true },
      { key: 'blockCode', label: 'Block Reference', type: 'number', required: true },
      { key: 'onCallStart', label: 'On-Call Start', type: 'datetime', required: true },
      { key: 'onCallEnd', label: 'On-Call End', type: 'datetime', required: true },
    ],
    filters: [
      { label: 'Search by Nurse', inputs: [{ key: 'nurseId', label: 'Staff ID', type: 'number', required: true }], path: (v) => `/oncall/nurse/${v['nurseId']}` },
      { label: 'Staffing at Specific Time', inputs: [{ key: 'time', label: 'Pick Time', type: 'datetime', required: true }], path: () => '/oncall/at', params: (v) => ({ time: String(v['time']) }) },
    ],
  },
  departments: {
    title: 'Hospital Departments',
    description: 'Medical and administrative departments and their appointed head physicians.',
    roles: shared,
    listPath: '/departments',
    createPath: '/admin/departments',
    columns: [
      { key: 'departmentId', label: 'Dept ID', type: 'number' },
      { key: 'name', label: 'Department Name', type: 'text' },
      { key: 'headName', label: 'Head Physician', type: 'text' },
    ],
    createFields: [
      { key: 'name', label: 'Department Name', type: 'text', required: true },
      { key: 'headId', label: 'Head Physician ID', type: 'number', required: true },
    ],
  },
  affiliations: {
    title: 'Department Staffing',
    description: 'Track physician affiliations across various hospital departments.',
    roles: shared,
    listPath: '/affiliations',
    createPath: '/admin/affiliations',
    columns: [
      { key: 'physicianName', label: 'Physician', type: 'text' },
      { key: 'departmentName', label: 'Department', type: 'text' },
      { key: 'primaryAffiliation', label: 'Primary Assignment?', type: 'boolean' },
    ],
    createFields: [
      { key: 'physicianId', label: 'Physician Staff ID', type: 'number', required: true },
      { key: 'departmentId', label: 'Department ID', type: 'number', required: true },
      { key: 'primaryAffiliation', label: 'Is Primary Assignment?', type: 'boolean' },
    ],
  },
  trainedin: {
    title: 'Staff Certifications',
    description: 'Certification records and procedure training for medical staff.',
    roles: shared,
    listPath: '/trainedin',
    createPath: '/admin/trainedin/trainings',
    columns: [
      { key: 'physicianName', label: 'Physician', type: 'text' },
      { key: 'treatmentName', label: 'Medical Procedure', type: 'text' },
      { key: 'certificationDate', label: 'Certified On', type: 'date' },
      { key: 'certificationExpires', label: 'Expiry Date', type: 'date' },
    ],
    createFields: [
      { key: 'physicianId', label: 'Physician Staff ID', type: 'number', required: true },
      { key: 'treatmentId', label: 'Procedure Code', type: 'number', required: true },
      { key: 'certificationDate', label: 'Certification Date', type: 'date', required: true },
      { key: 'certificationExpires', label: 'Certification Expiry', type: 'date', required: true },
    ],
    filters: [
      { label: 'Filter by Physician', inputs: [{ key: 'physicianId', label: 'Staff ID', type: 'number', required: true }], path: (v) => `/trainedin/physician/${v['physicianId']}` },
    ],
  },
};

@Component({
  selector: 'app-entity-page',
  standalone: true,
  imports: [DatePipe, FormsModule, NgFor, NgIf, ReactiveFormsModule, TitleCasePipe],
  templateUrl: './entity-page.component.html',
  styleUrls: ['./entity-page.component.css']
})
export class EntityPageComponent implements OnInit, OnDestroy {
  readonly config = signal<EntityConfig | null>(null);
  readonly rows = signal<Record<string, unknown>[]>([]);
  readonly loading = signal(false);
  readonly entityName = signal('');
  readonly activeFilterFields = signal<FieldConfig[]>([]);
  readonly form: UntypedFormGroup = this.fb.group({});
  readonly filterValues: Record<string, unknown> = {};
  readonly showCreateForm = signal(false);

  // Modal state
  readonly deleteModalTarget = signal<{row: Record<string, unknown>, cfg: EntityConfig} | null>(null);

  private subscription?: Subscription;

  constructor(
    readonly auth: AuthService,
    private readonly api: ApiService,
    private readonly fb: UntypedFormBuilder,
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly toast: ToastService
  ) {}

  ngOnInit(): void {
    this.subscription = this.route.paramMap.subscribe((params) => {
      const entity = params.get('entity') ?? '';
      const config = configs[entity];

      if (!config) {
        this.router.navigateByUrl('/dashboard');
        return;
      }

      if (!this.auth.hasAnyRole(config.roles)) {
        this.router.navigateByUrl(this.auth.isPatient() && this.auth.userId() ? `/patients/${this.auth.userId()}/dashboard` : '/dashboard');
        return;
      }

      this.entityName.set(entity);
      this.config.set(config);
      this.buildForm(config);
      this.activeFilterFields.set(config.filters?.[0]?.inputs ?? []);
      this.showCreateForm.set(false);
      this.load();
    });
  }

  ngOnDestroy(): void {
    this.subscription?.unsubscribe();
  }

  load(): void {
    const cfg = this.config();
    if (!cfg) return;

    const path = typeof cfg.listPath === 'function' ? cfg.listPath(this.auth) : cfg.listPath;
    this.fetch(path);
  }

  create(cfg: EntityConfig): void {
    if (!cfg.createPath || this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.loading.set(true);
    this.api.post<Record<string, unknown>>(cfg.createPath, this.cleanPayload(this.form.getRawValue())).subscribe({
      next: () => {
        this.toast.success(`${cfg.title} created successfully`);
        this.form.reset();
        this.buildForm(cfg);
        this.showCreateForm.set(false);
        this.load();
      },
      error: (error: Error) => {
        this.toast.error(error.message);
        this.loading.set(false);
      },
      complete: () => this.loading.set(false),
    });
  }

  edit(row: Record<string, unknown>, cfg: EntityConfig): void {
    if (!cfg.updatePath) return;

    this.api.put<Record<string, unknown>>(cfg.updatePath(row), row).subscribe({
      next: () => {
        this.toast.success('Record updated successfully');
        this.load();
      },
      error: (error: Error) => this.toast.error(error.message),
    });
  }

  requestDelete(row: Record<string, unknown>, cfg: EntityConfig): void {
    this.deleteModalTarget.set({ row, cfg });
  }

  confirmDelete(): void {
    const target = this.deleteModalTarget();
    if (!target) return;
    
    const { row, cfg } = target;
    if (!cfg.deletePath) return;

    this.deleteModalTarget.set(null);
    this.loading.set(true);

    this.api.delete<void>(cfg.deletePath(row), cfg.deleteParams?.(row)).subscribe({
      next: () => {
        this.toast.success('Record deleted successfully');
        this.load();
      },
      error: (error: Error) => {
        this.toast.error(error.message);
        this.loading.set(false);
      },
    });
  }

  runFilter(filter: FilterConfig): void {
    this.activeFilterFields.set(filter.inputs);
    const missingRequired = filter.inputs.some((field) => field.required && !this.filterValues[field.key]);
    if (missingRequired) {
      this.toast.info(`Enter values for ${filter.label.toLowerCase()} and click again`);
      return;
    }

    this.fetch(filter.path(this.filterValues, this.auth), filter.params?.(this.filterValues));
  }

  runAction(action: RowAction, row: Record<string, unknown>): void {
    action.run(row, this.api, this.toast, () => this.load());
  }

  inputType(field: FieldConfig): string {
    if (field.type === 'datetime') return 'datetime-local';
    return field.type;
  }

  display(row: Record<string, unknown>, field: FieldConfig): string {
    const value = row[field.key];
    if (value === null || value === undefined || value === '') return '-';

    if (field.type === 'boolean') return value ? 'Yes' : 'No';
    if (field.type === 'datetime') return new Date(String(value)).toLocaleString();

    if (this.isExpired(field, row)) return `${String(value)} - Expired`;

    return String(value);
  }

  isExpired(field: FieldConfig, row: Record<string, unknown>): boolean {
    return field.key === 'certificationExpires' && new Date(String(row[field.key])).getTime() < Date.now();
  }

  canCreate(cfg: EntityConfig): boolean {
    return Boolean(cfg.createPath && cfg.createFields?.length && this.auth.isAdmin());
  }

  hasActions(cfg: EntityConfig): boolean {
    return Boolean((cfg.updatePath || cfg.deletePath || cfg.rowActions?.length) && this.auth.isAdmin());
  }

  private fetch(path: string, params?: Record<string, string | number | boolean | null | undefined>): void {
    this.loading.set(true);

    this.api.get<Record<string, unknown>[] | Record<string, unknown>>(path, params).subscribe({
      next: (data) => this.rows.set(Array.isArray(data) ? data : [data]),
      error: (error: Error) => {
        this.toast.error(error.message);
        this.loading.set(false);
      },
      complete: () => this.loading.set(false),
    });
  }

  private buildForm(cfg: EntityConfig): void {
    Object.keys(this.form.controls).forEach((key) => this.form.removeControl(key));

    cfg.createFields?.forEach((field) => {
      const defaultValue = field.type === 'boolean' ? false : '';
      this.form.addControl(field.key, this.fb.control(defaultValue, field.required ? Validators.required : []));
    });
  }

  private cleanPayload(payload: Record<string, unknown>): Record<string, unknown> {
    return Object.entries(payload).reduce<Record<string, unknown>>((acc, [key, value]) => {
      if (value === '' || value === null || value === undefined) return acc;
      acc[key] = value;
      return acc;
    }, {});
  }
}
