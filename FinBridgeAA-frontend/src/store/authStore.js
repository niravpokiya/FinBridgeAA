import { create } from 'zustand'
import { persist } from 'zustand/middleware'

export const useAuthStore = create(
  persist(
    (set, get) => ({
      // State
      user: null,
      token: null,
      isAuthenticated: false,
      isLoading: true,

      // Actions
      login: async (token, user) => {
        localStorage.setItem('accessToken', token)
        set({ token, user, isAuthenticated: true, isLoading: false })
      },

      logout: async () => {
        try {
          // Import authAPI dynamically to avoid circular dependency
          const { authAPI } = await import('../lib/api')
          await authAPI.logout()
        } catch (error) {
          console.error('Logout error:', error)
        } finally {
          localStorage.removeItem('accessToken')
          localStorage.removeItem('user')
          set({ token: null, user: null, isAuthenticated: false, isLoading: false })
        }
      },

      updateUser: (userData) => {
        set((state) => ({
          user: { ...state.user, ...userData },
        }))
      },

      setLoading: (loading) => {
        set({ isLoading: loading })
      },

      checkAuth: () => {
        const token = localStorage.getItem('accessToken')
        const user = localStorage.getItem('user')
        
        if (token && user) {
          try {
            const parsedUser = JSON.parse(user)
            set({ token, user: parsedUser, isAuthenticated: true, isLoading: false })
          } catch (error) {
            console.error('Error parsing user data:', error)
            localStorage.removeItem('accessToken')
            localStorage.removeItem('user')
            set({ token: null, user: null, isAuthenticated: false, isLoading: false })
          }
        } else {
          set({ isLoading: false })
        }
      },
    }),
    {
      name: 'auth-storage',
      partialize: (state) => ({
        token: state.token,
        user: state.user,
        isAuthenticated: state.isAuthenticated,
      }),
    }
  )
)
