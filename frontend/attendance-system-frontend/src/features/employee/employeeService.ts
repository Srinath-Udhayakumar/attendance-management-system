import apiClient from '../../services/apiClient'
import { ENDPOINTS } from '../../services/endpoints'
import type {
  AttendanceResponse,
  EmployeeDashboardResponse,
  MonthlyAttendanceSummaryDTO,
  PageResponse,
} from '../../types'

export const employeeService = {
  checkIn: async (): Promise<AttendanceResponse> => {
    const response = await apiClient.post<AttendanceResponse>(ENDPOINTS.EMPLOYEE.CHECK_IN)
    return response.data
  },
  checkOut: async (): Promise<AttendanceResponse> => {
    const response = await apiClient.post<AttendanceResponse>(ENDPOINTS.EMPLOYEE.CHECK_OUT)
    return response.data
  },
  getTodayAttendance: async (): Promise<AttendanceResponse | null> => {
    try {
      const response = await apiClient.get<AttendanceResponse>(ENDPOINTS.EMPLOYEE.ATTENDANCE_TODAY)
      return response.data
    } catch {
      return null
    }
  },
  getAttendanceHistory: async (
    from?: string,
    to?: string,
    page = 0,
    size = 10,
  ): Promise<PageResponse<AttendanceResponse>> => {
    const params = new URLSearchParams({ page: String(page), size: String(size) })
    if (from) params.append('from', from)
    if (to) params.append('to', to)
    const response = await apiClient.get<PageResponse<AttendanceResponse>>(
      `${ENDPOINTS.EMPLOYEE.ATTENDANCE_HISTORY}?${params}`,
    )
    return response.data
  },
  getMonthlyAttendance: async (
    month: number,
    year: number,
  ): Promise<MonthlyAttendanceSummaryDTO> => {
    const response = await apiClient.get<MonthlyAttendanceSummaryDTO>(
      ENDPOINTS.EMPLOYEE.ATTENDANCE_MONTHLY(month, year),
    )
    return response.data
  },
  getDashboard: async (): Promise<EmployeeDashboardResponse> => {
    const response = await apiClient.get<EmployeeDashboardResponse>(ENDPOINTS.EMPLOYEE.DASHBOARD)
    return response.data
  },
}
