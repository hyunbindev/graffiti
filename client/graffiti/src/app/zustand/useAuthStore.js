import { create } from 'zustand';
import { persist } from 'zustand/middleware';

export const useAuthStore = create(
  persist(
    (set) => ({
      accessToken: null,
      isAuthenticated: false,
      uuid: null,
      nickName: null,
      profileImgeUrl: null,
      groups:null,
      selectedGroup:null,
      // Set user information
      setUserInfo: (uuid, nickName, profileImgeUrl) =>
        set(() => ({ uuid, nickName, profileImgeUrl })),

      // Set access token and authentication status
      setToken: (token) =>
        set(() => ({ accessToken: token, isAuthenticated: !!token })),

      setGroups: (groups)=>
        set(()=> ({groups: groups})),
      
      setSelectedGroup: (group)=>
        set(()=> ({selectedGroup: group})),
      
      // Clear user information and authentication status
      clearAuth: () =>
        set(() => ({
          accessToken: null,
          isAuthenticated: false,
          uuid: null,
          nickName: null,
          profileImgeUrl: null,
          groups:null,
        })),
    }),
    {
      name: 'auth-storage', // localStorage key
      getStorage: () => localStorage, // localStorage 사용
    }
  )
);
