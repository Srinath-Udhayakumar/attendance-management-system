export interface AuthResponse {
  token: string
  tokenType: string
}

export interface AuthMeResponse {
  id: string
  name: string
  email: string
  role: string
  employeeId: string
  department: string
}

export interface LoginRequest {
  email: string
  password: string
}

export interface RegisterRequest {
  name: string
  email: string
  password: string
  departmentId: string
  role?: string
}

export type AttendanceStatus = 'PRESENT' | 'ABSENT' | 'LATE' | 'HALF_DAY'

export interface AttendanceResponse {
  id: string
  date: string
  checkInTime: string | null
  checkOutTime: string | null
  status: AttendanceStatus
  totalHours: number
  lateApproved: boolean
}

export interface PageResponse<T> {
  content: T[]
  totalElements: number
  totalPages: number
  size: number
  number: number
  first: boolean
  last: boolean
}

export interface EmployeeDashboardResponse {
  todayStatus: string
  presentCount: number
  absentCount: number
  lateCount: number
  halfDayCount: number
  totalHoursThisMonth: number
  last7DaysAttendance: DailyAttendanceDTO[]
}

export interface DailyAttendanceDTO {
  date: string
  status: AttendanceStatus
  hoursWorked: number
}

export interface ManagerDashboardResponse {
  totalEmployees: number
  presentToday: number
  absentToday: number
  lateToday: number
  weeklyTrend: WeeklyTrendDTO[]
  departmentStats: DepartmentStatDTO[]
  absentEmployeesToday: AbsentEmployeeDTO[]
}

export interface WeeklyTrendDTO {
  date: string
  present: number
  absent: number
  late: number
}

export interface DepartmentStatDTO {
  departmentName: string
  totalEmployees: number
  presentToday: number
  absentToday: number
  lateToday: number
  averageHoursWorked: number
}

export interface AbsentEmployeeDTO {
  name: string
  employeeId: string
  departmentName: string
  email: string
}

export interface MonthlyAttendanceSummaryDTO {
  presentDays: number
  absentDays: number
  lateDays: number
  halfDayCount: number
  totalHoursWorked: number
  startDate: string
  endDate: string
  workingDays: number
  attendancePercentage: number
}

export interface TeamSummaryResponse {
  totalEmployees: number
  presentToday: number
  absentToday: number
  lateToday: number
  halfDayToday: number
}

export interface TodayStatusResponse {
  presentEmployees: EmployeeInfo[]
  absentEmployees: EmployeeInfo[]
  lateEmployees: EmployeeInfo[]
}

export interface EmployeeInfo {
  id: string
  name: string
  employeeId: string
  department: string
  checkInTime: string | null
}
