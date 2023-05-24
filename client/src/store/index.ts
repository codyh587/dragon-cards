import axios from 'axios'
import { createStore } from 'vuex'
import { State, userData } from '../types/DragonCards.interface'

export default createStore({
  state: {
    username: '',
    token: '',
    sessionExpired: false
  } as State,

  getters: {
    username(state: State): string {
      return state.username
    },
    token(state: State): string {
      return state.token
    },
    sessionExpired(state: State): boolean {
      return state.sessionExpired
    }
  },

  mutations: {
    SET_USERNAME(state: State, username: string) {
      state.username = username
    },
    SET_TOKEN(state: State, token: string) {
      state.token = token
      axios.defaults.headers.common['token'] = state.token
    },
    SET_SESSIONEXPIRED(state: State, sessionExpired: boolean) {
      state.sessionExpired = sessionExpired
    }
  },

  actions: {
    login({ commit }, user: userData): void {
      commit('SET_USERNAME', user.username)
      commit('SET_TOKEN', user.token)
      commit('SET_SESSIONEXPIRED', false)
    },
    logout({ commit }): void {
      commit('SET_USERNAME', '')
      commit('SET_TOKEN', '')
      commit('SET_SESSIONEXPIRED', false)
    },
    expired({ commit }): void {
      commit('SET_SESSIONEXPIRED', true)
    }
  }
});
