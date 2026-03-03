import { createContext, useContext, useEffect, useState } from 'react'
import type { ReactNode } from 'react'
import type { AuthMeResponse, LoginRequest, RegisterRequest } from '../../types'
import { authService } from '../../features/auth/authService'

interface AuthContextType {
  user: AuthMeResponse | null
  loading: boolean
  login: (data: LoginRequest) => Promise<void>
  register: (data: RegisterRequest) => Promise<void>
  logout: () => void
}

export const AuthContext = createContext<AuthContextType | null>(null)

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<AuthMeResponse | null>(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const token = localStorage.getItem('token')
    if (!token) {
      setLoading(false)
      return
    }
    authService
      .getMe()
      .then(setUser)
      .catch(() => localStorage.removeItem('token'))
      .finally(() => setLoading(false))
  }, [])

  const login = async (data: LoginRequest) => {
    const response = await authService.login(data)
    localStorage.setItem('token', response.token)
    const me = await authService.getMe()
    setUser(me)
  }

  const register = async (data: RegisterRequest) => {
    const response = await authService.register(data)
    localStorage.setItem('token', response.token)
    const me = await authService.getMe()
    setUser(me)
  }

  const logout = () => {
    localStorage.removeItem('token')
    setUser(null)
  }

  return (
    <AuthContext.Provider value={{ user, loading, login, register, logout }}>
      {children}
    </AuthContext.Provider>
  )
}

export function useAuthContext() {
  const ctx = useContext(AuthContext)
  if (!ctx) throw new Error('useAuthContext must be used within AuthProvider')
  return ctx
}
