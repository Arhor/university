import axios from 'axios'

const login = async ({ commit }, { email, password }) => {
  const { data } = await axios.post('http://localhost:8080/api/v1/auth/signin', { email, password })
  commit('SET_AUTH', data)
}

const logout = ({ commit }) => commit('INVALIDATE')

const refresh = async ({ getters, commit, dispatch }) => {
  try {
    const { data } = await axios.get('http://localhost:8080/api/v1/auth/refresh', {
      headers: {
        Authorization: getters.authToken
      }
    })
    commit('SET_AUTH', data)
  } catch (error) {
    dispatch('logout')
  }
}

export default {
  login,
  logout,
  refresh,
}
