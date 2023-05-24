<template>
    <div class="container my-5">
        <div class="card">
            <div class="card-header">
                <h4 class="mt-1">Create Dragon</h4>
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
                <button type="button" @click="saveDragon" class="btn btn-primary">
                    Save
                </button>
                <router-link to="/dragons/" class="btn btn-secondary ms-2">
                    Back
                </router-link>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import axios from 'axios'
import { defineComponent } from 'vue'
import { Dragon } from '../../types/DragonCards.interface'

export default defineComponent({
    name: 'dragonsCreate',

    data() {
        return {
            errorList: [] as string[],
            dragon: {} as Dragon
        }
    },

    methods: {
        saveDragon(): void {
            this.errorList = [];
            console.log(this.dragon);

            let att: keyof typeof this.dragon;
            for (att in this.dragon) {
                if (!this.dragon[att]) {
                    this.errorList.push('"' + att + '" is required');
                }
            }

            if (this.errorList.length == 0) {
                axios.post('/dragons', this.dragon)
                    .then(res => {
                        console.log(res)

                        this.dragon = {
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
