<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'
</script>

<template>
    <header>
        <div class="wrapper">
            <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
                <div class="container-fluid">
                    <RouterLink class="navbar-brand" to="/">
                        <svg class="logo" viewBox="0 0 128 128" width="28" height="28">
                            <path fill="#42b883" d="M78.8,10L64,35.4L49.2,10H0l64,110l64-110C128,10,78.8,10,78.8,10z">
                            </path>
                            <path fill="#35495e" d="M78.8,10L64,35.4L49.2,10H25.6L64,76l38.4-66H78.8z">
                            </path>
                        </svg>
                    </RouterLink>
                    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                        <span class="navbar-toggler-icon"></span>
                    </button>
                    <div class="collapse navbar-collapse" id="navbarSupportedContent">
                        <ul class="navbar-nav ms-auto mb-2 mb-lg-0">
                            <li class="nav-item">
                                <RouterLink class="nav-link active" to="/">
                                    Home
                                </RouterLink>
                            </li>
                            <li class="nav-item">
                                <RouterLink class="nav-link" to="/dragons">
                                    Dragons
                                </RouterLink>
                            </li>
                        </ul>
                    </div>
                </div>
            </nav>
            <div class="alert alert-danger" v-if="this.$store.getters.sessionExpired" role="alert">
                Session expired. Please log in again to continue.
            </div>
            <div class="alert alert-warning" v-if="!this.$store.getters.token && this.$route.name !== 'home'" role="alert">
                Log in to access the application.
            </div>
        </div>
    </header>
    <RouterView />
</template>
