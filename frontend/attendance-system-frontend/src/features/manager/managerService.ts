import apiClient from '../../services/apiClient'
import { ENDPOINTS } from '../../services/endpoints'
import type {
  AttendanceResponse,
  ManagerDashboardResponse,
  PageResponse,
  TeamSummaryResponse,
  TodayStatusResponse,
} from '../../types'

export const managerService = {
  getAllAttendances: async (
    date?: string,
    page = 0,
    size = 10,
  ): Promise<PageResponse<AttendanceResponse>> => {
    const params = new URLSearchParams({ page: String(page), size: String(size) })
    if (date) params.append('date', date)
    const response = await apiClient.get<PageResponse<AttendanceResponse>>(
      `${ENDPOINTS.MANAGER.ATTENDANCE}?${params}`,
    )
    return response.data
  },
  getEmployeeAttendance: async (
    userId: string,
    from?: string,
    to?: string,
    page = 0,
    size = 10,
  ): Promise<PageResponse<AttendanceResponse>> => {
    const params = new URLSearchParams({ page: String(page), size: String(size) })
    if (from) params.append('from', from)
    if (to) params.append('to', to)
    const response = await apiClient.get<PageResponse<AttendanceResponse>>(
      `${ENDPOINTS.MANAGER.EMPLOYEE_ATTENDANCE(userId)}?${params}`,
    )
    return response.data
  },
  approveLate: async (attendanceId: string): Promise<AttendanceResponse> => {
    const response = await apiClient.post<AttendanceResponse>(
      ENDPOINTS.MANAGER.APPROVE_LATE(attendanceId),
    )
    return response.data
  },
  getDashboard: async (): Promise<ManagerDashboardResponse> => {
    const response = await apiClient.get<ManagerDashboardResponse>(ENDPOINTS.MANAGER.DASHBOARD)
    return response.data
  },
  exportCSV: async (from?: string, to?: string): Promise<void> => {
    const params = new URLSearchParams()
    if (from) params.append('from', from)
    if (to) params.append('to', to)
    const response = await apiClient.get(`${ENDPOINTS.MANAGER.EXPORT_CSV}?${params}`, {
      responseType: 'blob',
    })
    const url = window.URL.createObjectURL(new Blob([response.data as BlobPart]))
    const link = document.createElement('a')
    link.href = url
    link.setAttribute('download', `attendance_${from ?? 'start'}_to_${to ?? 'end'}.csv`)
    document.body.appendChild(link)
    link.click()
    link.remove()
    window.URL.revokeObjectURL(url)
  },
  getTeamSummary: async (date?: string): Promise<TeamSummaryResponse> => {
    const params = date ? `?date=${date}` : ''
    const response = await apiClient.get<TeamSummaryResponse>(
      `${ENDPOINTS.ATTENDANCE.SUMMARY}${params}`,
    )
    return response.data
  },
  getTodayStatus: async (): Promise<TodayStatusResponse> => {
    const response = await apiClient.get<TodayStatusResponse>(ENDPOINTS.ATTENDANCE.TODAY_STATUS)
    return response.data
  },
}
