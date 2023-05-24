<template>
    <main>
        <div class="alert alert-success" v-if="this.registerSuccess" role="alert">
            Registration successful!
        </div>
        <div class="alert alert-danger" v-if="this.registerError" role="alert">
            Registration failed: username already exists.
        </div>
        <div class="alert alert-danger" v-if="loginError" role="alert">
            Login failed. Please try again.
        </div>
        <div class="container my-5 w-50" v-if="$store.getters.token">
            <h1>Welcome, {{ $store.getters.username }}!</h1>
        </div>
        <div class="container my-5 w-50">
            <div class="card">
                <div class="card-header">
                    <h4 class="mt-1">Log In</h4>
                </div>
                <div class="card-body" v-if="!$store.getters.token || $store.getters.sessionExpired">
                    <ul class="alert alert-warning" v-if="Object.keys(errorList).length > 0">
                        <li class="m-b0 ms-3" v-for="(err, index) in errorList" :key="index">
                            {{ err }}
                        </li>
                    </ul>
                    <div class="mb-3">
                        <label for="">Username</label>
                        <input type="text" v-model="user.username" class="form-control" />
                    </div>
                    <div class="mb-3">
                        <label for="">Password</label>
                        <input type="password" v-model="user.password" class="form-control" />
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
import { defineComponent } from 'vue'
import { loginForm, userData } from '../types/DragonCards.interface'

export default defineComponent({
    name: 'homeView',

    data() {
        return {
            registerError: false,
            registerSuccess: false,
            loginError: false,
            errorList: [] as string[],
            user: {} as loginForm
        }
    },

    methods: {
        login(): void {
            if (this.checkForm()) { return; }

            axios.post('/login', this.user)
                .then(res => {
                    console.log(res)
                    let sessionInfo: userData = {
                        username: res.data.user.username,
                        token: res.data.token
                    }
                    this.$store.dispatch('login', sessionInfo)
                })
                .catch(err => {
                    console.log(err)
                    this.loginError = true
                });
        },

        register(): void {
            if (this.checkForm()) { return; }

            axios.post('/register', this.user)
                .then(res => {
                    console.log(res)
                    this.registerSuccess = true
                })
                .catch(err => {
                    console.log(err)
                    this.registerError = true
                });
        },

        logout(): void {
            this.$store.dispatch('logout');
            this.user = {
                username: '',
                password: ''
            };
        },

        checkForm(): boolean {
            this.registerSuccess = false;
            this.registerError = false;
            this.loginError = false;
            this.errorList = [];

            console.log(this.user);
            let error = false;

            let att: keyof typeof this.user;
            for (att in this.user) {
                if (!this.user[att]) {
                    this.errorList.push('"' + att + '" is required');
                    error = true;
                }
            }

            return error;
        }
    }
});
</script>
