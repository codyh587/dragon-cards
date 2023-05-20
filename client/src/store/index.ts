import { createStore } from 'vuex'

interface storeState {
    username: string;
    token: string;
    sessionExpired: boolean
}

interface userData {
    username: string;
    token: string
}

const store = createStore({
    state: {
        username: '',
        token: '',
        sessionExpired: false
    } as storeState,

    getters: {
        username(state) {
            return state.username
        },

        token(state) {
            return state.token
        },

        sessionExpired(state) {
            return state.sessionExpired
        }
    },

    mutations: {
        SET_USERNAME(state, username: string) {
            state.username = username
        },

        SET_TOKEN(state, token: string) {
            state.token = token
        },

        SET_SESSIONEXPIRED(state, sessionExpired: boolean) {
            state.sessionExpired = sessionExpired
        }
    },

    actions: {
        async setUsername({ commit }, username: string) {
            commit('SET_USERNAME', username)
        },

        async setToken({ commit }, token: string) {
            commit('SET_TOKEN', token)
        },

        async setSessionExpired({ commit }, sessionExpired: boolean) {
            commit('SET_SESSIONEXPIRED', sessionExpired)
        },

        async login({ dispatch }, user: userData) {
            dispatch('setUsername', user.username)
            dispatch('setToken', user.token)
            dispatch('setSessionExpired', false)
        },

        async logout({ dispatch }) {
            dispatch('setUsername', '')
            dispatch('setToken', '')
            dispatch('setSessionExpired', false)
        },

        async expired({ dispatch }) {
            dispatch('setSessionExpired', true)
        }
    }
})

export default store
