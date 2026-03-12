import { Link, useNavigate } from 'react-router-dom'
import type { AuthMeResponse } from '../../types'

interface Props {
  user: AuthMeResponse | null
  onLogout: () => void
}

export default function Navbar({ user, onLogout }: Props) {
  const navigate = useNavigate()

  const handleLogout = () => {
    onLogout()
    navigate('/login')
  }

  return (
    <nav className="bg-white border-b border-gray-200 px-4 py-3">
      <div className="max-w-7xl mx-auto flex items-center justify-between">
        <Link to="/" className="flex items-center gap-2 text-blue-600 font-bold text-lg">
          <span>🕐</span>
          <span>AttendanceMS</span>
        </Link>
        {user && (
          <div className="flex items-center gap-4">
            <div className="text-right hidden sm:block">
              <p className="text-sm font-medium text-gray-900">{user.name}</p>
              <p className="text-xs text-gray-500">{user.role} · {user.department}</p>
            </div>
            <button
              onClick={handleLogout}
              className="text-sm text-gray-600 hover:text-red-600 transition-colors"
            >
              Sign out
            </button>
          </div>
        )}
      </div>
    </nav>
  )
}
