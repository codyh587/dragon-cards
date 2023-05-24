<template>
    <header>
        <div class="wrapper">
            <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
                <div class="container-fluid">
                    <router-link class="navbar-brand" to="/">
                        <svg class="logo" viewBox="0 0 128 128" width="28" height="28">
                            <path fill="#42b883" d="M78.8,10L64,35.4L49.2,10H0l64,110l64-110C128,10,78.8,10,78.8,10z">
                            </path>
                            <path fill="#35495e" d="M78.8,10L64,35.4L49.2,10H25.6L64,76l38.4-66H78.8z">
                            </path>
                        </svg>
                        Dragon Cards
                    </router-link>
                    <div class="navbar navbar-dark bg-dark">
                        <ul class="navbar-nav ms-auto mb-2 mb-lg-0">
                            <li class="nav-item">
                                <router-link class="nav-link active" to="/">
                                    Home
                                </router-link>
                            </li>
                            <li class="nav-item">
                                <router-link class="nav-link" to="/dragons">
                                    Dragons
                                </router-link>
                            </li>
                        </ul>
                    </div>
                </div>
            </nav>
            <div class="alert alert-danger my-0" v-if="$store.getters.sessionExpired" role="alert">
                Session expired. Please log in again to continue.
            </div>
            <div class="alert alert-warning my-0" v-if="!$store.getters.token && $route.name !== 'homeView'" role="alert">
                Log in to access the application.
            </div>
        </div>
    </header>
    <router-view />
</template>
