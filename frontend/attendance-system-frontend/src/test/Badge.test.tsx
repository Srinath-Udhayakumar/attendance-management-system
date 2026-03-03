import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import Badge from '../components/ui/Badge'

describe('Badge', () => {
  it('renders status', () => {
    render(<Badge status="PRESENT" />)
    expect(screen.getByText('PRESENT')).toBeInTheDocument()
  })

  it('renders custom label', () => {
    render(<Badge status="LATE" label="Late Arrival" />)
    expect(screen.getByText('Late Arrival')).toBeInTheDocument()
  })
})
