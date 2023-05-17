<template>
  <div class="container">
    <div class="card">
      <div class="card-header">
        <h4>
          Dragons
          <RouterLink to="/dragon/create" class="btn btn-primary float-end">
            Create Dragon
          </RouterLink>
        </h4>
      </div>
      <div class="card-body">
        <table class="table table-bordered">
          <thead>
            <tr>
              <th>Name</th>
              <th>Description</th>
              <th>Family</th>
              <th>City Spotted</th>
              <th>Neighborhood Spotted</th>
              <th>State Spotted</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody v-if="this.dragons.length > 0">
            <tr v-for="(dragon, index) in this.dragons" :key="index">
              <td>{{ dragon.name }}</td>
              <td>{{ dragon.description }}</td>
              <td>{{ dragon.family }}</td>
              <td>{{ dragon.spottedCity }}</td>
              <td>{{ dragon.spottedNeighborhood }}</td>
              <td>{{ dragon.spottedState }}</td>
              <td class="form-inline">
                <RouterLink to="/" class="btn btn-success">
                  Edit
                </RouterLink>
                <button type="button" class="btn btn-danger">
                  Delete
                </button>
              </td>
            </tr>
          </tbody>
          <tbody v-else>
            <tr>
              <td colspan="7">
                Loading...
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import axios from 'axios'

export default {
  name: 'dragons',
  
  data() {
    return {
      dragons: []
    }
  },
  
  mounted() {
    this.getDragons();
  },
  
  methods: {
    getDragons() {
      // TODO replace mock API Endpoint 
      axios.get('https://de5613ea-edb7-47c8-97b1-6e852c155697.mock.pstmn.io/dragons').then(res => {
        this.dragons = res.data
        console.log(this.dragons)
      });
    }
  },
}
</script>
