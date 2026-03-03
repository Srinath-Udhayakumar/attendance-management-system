import { useAuthContext } from '../providers/AuthContext'
import AppLayout from '../../components/layout/AppLayout'

export default function AuthLayout() {
  const { user, logout } = useAuthContext()
  return <AppLayout user={user} onLogout={logout} />
}
