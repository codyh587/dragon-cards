<template>
    <main>
        <div class="container my-5 w-50" v-if="this.$store.getters.token">
            <h1>Welcome, {{ this.$store.getters.username }}!</h1>
        </div>
        <div class="container my-5 w-50">
            <div class="card">
                <div class="card-header">
                    <h4 class="mt-1">Log In</h4>
                </div>
                <div class="card-body">
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
                        <input type="text" v-model="model.user.password" class="form-control" />
                    </div>
                    <div class="mb-3">
                        <button type="button" @click="login" class="btn btn-primary">
                            Login
                        </button>
                        <button type="button" @click="signup" class="btn btn-secondary ms-2">
                            Sign up
                        </button>
                    </div>
                </div>
            </div>
        </div>
        <button type="button" @click="test" class="btn btn-primary">
            test
        </button>
    </main>
</template>

<script lang="ts">
export default {
    name: 'home',

    data() {
        return {
            registerError: false,
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
        test(): void {
            let user = {
                username: "test",
                token: "lol"
            }
            this.$store.dispatch('login', user)
        },

        login(): void {
            this.checkForm()
            console.log(this.model.user)
        },

        signup(): void {
            this.checkForm()
            console.log(this.model.user)
        },
        
        checkForm(): void {
            this.errorList = [];

            for (const att in this.model.user) {
                if (this.model.user[att] === '') {
                    this.errorList.push('"' + att + '" is required');
                }
            }
        }
    }
}
</script>
