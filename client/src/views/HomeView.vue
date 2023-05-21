<template>
    <main>
        <div class="alert alert-success" v-if="this.registerSuccess" role="alert">
            Registration successful!
        </div>
        <div class="alert alert-danger" v-if="this.registerError" role="alert">
            Registration failed: username already exists.
        </div>
        <div class="alert alert-danger" v-if="this.loginError" role="alert">
            Login failed. Please try again.
        </div>
        <div class="container my-5 w-50" v-if="this.$store.getters.token">
            <h1>Welcome, {{ this.$store.getters.username }}!</h1>
        </div>
        <div class="container my-5 w-50">
            <div class="card">
                <div class="card-header">
                    <h4 class="mt-1">Log In</h4>
                </div>
                <div class="card-body" v-if="!this.$store.getters.token || this.$store.getters.sessionExpired">
                    <ul class="alert alert-warning" v-if="Object.keys(this.errorList).length > 0">
                        <li class="m-b0 ms-3" v-for="(err, index) in this.errorList" :key="index">
                            {{ err }}
                        </li>
                    </ul>
                    <div class="mb-3">
                        <label for="">Username</label>
                        <input type="text" v-model="model.user.username" class="form-control" />
                    </div>
                    <div class="mb-3">
                        <label for="">Password</label>
                        <input type="password" v-model="model.user.password" class="form-control" />
                    </div>
                    <button type="button" @click="login" class="btn btn-primary">
                        Login
                    </button>
                    <button type="button" @click="register" class="btn btn-secondary ms-2">
                        Sign up
                    </button>
                </div>
                <div class="card-body" v-else>
                    <button type="button" @click="logout" class="btn btn-primary">
                        Sign out
                    </button>
                </div>
            </div>
        </div>
    </main>
</template>

<script lang="ts">
import axios from 'axios'

export default {
    name: 'home',

    data() {
        return {
            registerError: false,
            registerSuccess: false,
            loginError: false,
            errorList: Array<str>,

            model: {
                user: {
                    username: '',
                    password: ''
                }
            }
        }
    },

    methods: {
        login(): void {
            if(this.checkForm()) { return }
            const pointer = this

            axios.post('/login', this.model.user)
                .then(res => {
                    console.log(res)
                    let sessionInfo = {
                        username: res.data.user.username,
                        token: res.data.token
                    }

                    this.$store.dispatch('login', sessionInfo)
                })
                .catch(function (err) {
                    console.log(err)
                    pointer.loginError = true
                });
        },

        register(): void {
            if(this.checkForm()) { return }
            const pointer = this

            axios.post('/register', this.model.user)
                .then(res => {
                    console.log(res)
                    this.registerSuccess = true
                })
                .catch(function (err) {
                    console.log(err)
                    pointer.registerError = true
                });
        },

        logout(): void {
            this.$store.dispatch('logout')
            this.model.user = {
                username: "",
                password: ""
            }
        },

        checkForm(): boolean {
            this.registerSuccess = false
            this.registerError = false
            this.loginError = false
            this.errorList = []

            console.log(this.model.user)
            let error: boolean = false

            for (const att in this.model.user) {
                if (this.model.user[att] === '') {
                    this.errorList.push('"' + att + '" is required');
                    error = true
                }
            }

            return error
        }
    }
}
</script>
