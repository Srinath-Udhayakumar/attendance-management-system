import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import StatCard from '../components/ui/StatCard'

describe('StatCard', () => {
  it('renders title and value', () => {
    render(<StatCard title="Present" value={5} icon="✅" />)
    expect(screen.getByText('Present')).toBeInTheDocument()
    expect(screen.getByText('5')).toBeInTheDocument()
    expect(screen.getByText('✅')).toBeInTheDocument()
  })
})
