import { createBrowserRouter, Navigate } from 'react-router-dom'
import LoginPage from '../../pages/LoginPage'
import RegisterPage from '../../pages/RegisterPage'
import EmployeeDashboardPage from '../../pages/EmployeeDashboardPage'
import AttendancePage from '../../pages/AttendancePage'
import ManagerDashboardPage from '../../pages/ManagerDashboardPage'
import ManagerAttendancePage from '../../pages/ManagerAttendancePage'
import ManagerTodayPage from '../../pages/ManagerTodayPage'
import ProtectedRoute from './ProtectedRoute'
import AuthLayout from './AuthLayout'

export const router = createBrowserRouter([
  { path: '/login', element: <LoginPage /> },
  { path: '/register', element: <RegisterPage /> },
  {
    element: <ProtectedRoute />,
    children: [
      {
        element: <AuthLayout />,
        children: [
          { path: '/', element: <Navigate to="/dashboard" replace /> },
          { path: '/dashboard', element: <EmployeeDashboardPage /> },
          { path: '/attendance', element: <AttendancePage /> },
          { path: '/manager/dashboard', element: <ManagerDashboardPage /> },
          { path: '/manager/attendance', element: <ManagerAttendancePage /> },
          { path: '/manager/today', element: <ManagerTodayPage /> },
        ],
      },
    ],
  },
  { path: '*', element: <Navigate to="/login" replace /> },
])
