<template>
    <div class="container my-5">
        <div class="card">
            <div class="card-header">
                <h4 class="mt-1">Edit Dragon</h4>
            </div>
            <div class="card-body">
                <ul class="alert alert-warning" v-if="Object.keys(this.errorList).length > 0">
                    <li class="m-b0 ms-3" v-for="(err, index) in this.errorList" :key="index">
                        {{ err }}
                    </li>
                </ul>
                <div class="mb-3">
                    <label for="">Name</label>
                    <input type="text" v-model="dragon.name" class="form-control" />
                </div>
                <div class="mb-3">
                    <label for="">Description</label>
                    <input type="text" v-model="dragon.description" class="form-control" />
                </div>
                <div class="mb-3">
                    <label for="">Family</label>
                    <input type="text" v-model="dragon.family" class="form-control" />
                </div>
                <div class="mb-3">
                    <label for="">City Spotted</label>
                    <input type="text" v-model="dragon.spottedCity" class="form-control" />
                </div>
                <div class="mb-3">
                    <label for="">Neighborhood Spotted</label>
                    <input type="text" v-model="dragon.spottedNeighborhood" class="form-control" />
                </div>
                <div class="mb-3">
                    <label for="">State Spotted</label>
                    <input type="text" v-model="dragon.spottedState" class="form-control" />
                </div>
                <button type="button" @click="editDragon" class="btn btn-primary">
                    Update
                </button>
                <router-link to="/dragons" class="btn btn-secondary ms-2">
                    Back
                </router-link>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import axios from 'axios'
import { defineComponent } from 'vue'
import { EditDragon } from '../../types/DragonCards.interface'

export default defineComponent({
    name: 'dragonsEdit',

    data() {
        return {
            errorList: [] as string[],
            dragon: {
                oldName: this.$route.params.name
            } as EditDragon
        }
    },

    methods: {
        editDragon(): void {
            this.errorList = [];
            console.log(this.dragon);

            if (!this.dragon.name) {
                this.errorList.push('"name" is required');
            }

            if (this.errorList.length == 0) {
                axios.patch('/dragons', this.dragon)
                    .then(res => {
                        console.log(res)

                        this.dragon = {
                            ...this.dragon,
                            name: '',
                            description: '',
                            family: '',
                            spottedCity: '',
                            spottedNeighborhood: '',
                            spottedState: ''
                        }

                        setTimeout(() => '', 1000)
                        this.$router.push('/dragons')
                    })
                    .catch(err => {
                        console.log(err)
                        if (this.$store.getters.token) {
                            this.$store.dispatch('expired')
                        }
                    });
            }
        }
    }
});
</script>
