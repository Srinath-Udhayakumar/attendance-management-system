export const ENDPOINTS = {
  AUTH: {
    LOGIN: '/api/auth/login',
    REGISTER: '/api/auth/register',
    ME: '/api/auth/me',
  },
  EMPLOYEE: {
    CHECK_IN: '/api/employee/check-in',
    CHECK_OUT: '/api/employee/check-out',
    ATTENDANCE_TODAY: '/api/employee/attendance/today',
    ATTENDANCE_HISTORY: '/api/employee/attendance/history',
    ATTENDANCE_MONTHLY: (month: number, year: number) =>
      `/api/employee/attendance/monthly/${month}/${year}`,
    DASHBOARD: '/api/employee/dashboard',
  },
  MANAGER: {
    ATTENDANCE: '/api/manager/attendance',
    EMPLOYEE_ATTENDANCE: (userId: string) => `/api/manager/attendance/${userId}`,
    APPROVE_LATE: (attendanceId: string) =>
      `/api/manager/attendance/${attendanceId}/approve-late`,
    DASHBOARD: '/api/manager/dashboard',
    EXPORT_CSV: '/api/manager/export/csv',
  },
  ATTENDANCE: {
    SUMMARY: '/api/attendance/summary',
    TODAY_STATUS: '/api/attendance/today-status',
  },
}
