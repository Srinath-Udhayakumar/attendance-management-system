import { render, screen, fireEvent, waitFor } from '@testing-library/react'
import { describe, it, expect, vi, beforeEach } from 'vitest'
import { MemoryRouter } from 'react-router-dom'
import LoginPage from '../pages/LoginPage'
import { AuthContext } from '../app/providers/AuthContext'

const mockLogin = vi.fn()
const mockContextValue = {
  user: null,
  loading: false,
  login: mockLogin,
  register: vi.fn(),
  logout: vi.fn(),
}

function renderWithContext(ui: React.ReactElement) {
  return render(
    <AuthContext.Provider value={mockContextValue}>
      <MemoryRouter>
        {ui}
      </MemoryRouter>
    </AuthContext.Provider>,
  )
}

describe('LoginPage', () => {
  beforeEach(() => {
    mockLogin.mockReset()
  })

  it('renders login form', () => {
    renderWithContext(<LoginPage />)
    expect(screen.getByPlaceholderText('you@example.com')).toBeInTheDocument()
    expect(screen.getByPlaceholderText('••••••')).toBeInTheDocument()
    expect(screen.getByRole('button', { name: /sign in/i })).toBeInTheDocument()
  })

  it('calls login with form values on submit', async () => {
    mockLogin.mockResolvedValue(undefined)
    renderWithContext(<LoginPage />)

    fireEvent.change(screen.getByPlaceholderText('you@example.com'), {
      target: { value: 'test@example.com' },
    })
    fireEvent.change(screen.getByPlaceholderText('••••••'), {
      target: { value: 'password123' },
    })
    fireEvent.click(screen.getByRole('button', { name: /sign in/i }))

    await waitFor(() => {
      expect(mockLogin).toHaveBeenCalledWith({
        email: 'test@example.com',
        password: 'password123',
      })
    })
  })

  it('shows error message on login failure', async () => {
    mockLogin.mockRejectedValue({ response: { data: { message: 'Invalid credentials' } } })
    renderWithContext(<LoginPage />)

    fireEvent.change(screen.getByPlaceholderText('you@example.com'), {
      target: { value: 'wrong@example.com' },
    })
    fireEvent.change(screen.getByPlaceholderText('••••••'), {
      target: { value: 'wrongpass' },
    })
    fireEvent.click(screen.getByRole('button', { name: /sign in/i }))

    await waitFor(() => {
      expect(screen.getByText('Invalid credentials')).toBeInTheDocument()
    })
  })
})
