import apiClient from '../../services/apiClient'
import { ENDPOINTS } from '../../services/endpoints'
import type { AuthMeResponse, AuthResponse, LoginRequest, RegisterRequest } from '../../types'

export const authService = {
  login: async (data: LoginRequest): Promise<AuthResponse> => {
    const response = await apiClient.post<AuthResponse>(ENDPOINTS.AUTH.LOGIN, data)
    return response.data
  },
  register: async (data: RegisterRequest): Promise<AuthResponse> => {
    const response = await apiClient.post<AuthResponse>(ENDPOINTS.AUTH.REGISTER, data)
    return response.data
  },
  getMe: async (): Promise<AuthMeResponse> => {
    const response = await apiClient.get<AuthMeResponse>(ENDPOINTS.AUTH.ME)
    return response.data
  },
}
