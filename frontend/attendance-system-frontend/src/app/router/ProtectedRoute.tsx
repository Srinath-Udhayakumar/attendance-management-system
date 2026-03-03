import { Navigate, Outlet } from 'react-router-dom'
import { useAuthContext } from '../providers/AuthContext'
import LoadingSpinner from '../../components/common/LoadingSpinner'

export default function ProtectedRoute() {
  const { user, loading } = useAuthContext()

  if (loading) {
    return (
      <div className="flex h-screen items-center justify-center">
        <LoadingSpinner size="lg" />
      </div>
    )
  }

  return user ? <Outlet /> : <Navigate to="/login" replace />
}
