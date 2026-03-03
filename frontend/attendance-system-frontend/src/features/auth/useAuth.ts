import { useState, useCallback } from 'react'
import type { AuthMeResponse, LoginRequest, RegisterRequest } from '../../types'
import { authService } from './authService'

export function useAuth() {
  const [user, setUser] = useState<AuthMeResponse | null>(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const login = useCallback(async (data: LoginRequest) => {
    setLoading(true)
    setError(null)
    try {
      const response = await authService.login(data)
      localStorage.setItem('token', response.token)
      const me = await authService.getMe()
      setUser(me)
      return me
    } catch (err: unknown) {
      const message = err instanceof Error ? err.message : 'Login failed'
      setError(message)
      throw err
    } finally {
      setLoading(false)
    }
  }, [])

  const register = useCallback(async (data: RegisterRequest) => {
    setLoading(true)
    setError(null)
    try {
      const response = await authService.register(data)
      localStorage.setItem('token', response.token)
      const me = await authService.getMe()
      setUser(me)
      return me
    } catch (err: unknown) {
      const message = err instanceof Error ? err.message : 'Registration failed'
      setError(message)
      throw err
    } finally {
      setLoading(false)
    }
  }, [])

  const logout = useCallback(() => {
    localStorage.removeItem('token')
    setUser(null)
  }, [])

  const fetchMe = useCallback(async () => {
    const token = localStorage.getItem('token')
    if (!token) return
    setLoading(true)
    try {
      const me = await authService.getMe()
      setUser(me)
    } catch {
      localStorage.removeItem('token')
    } finally {
      setLoading(false)
    }
  }, [])

  return { user, loading, error, login, register, logout, fetchMe }
}
