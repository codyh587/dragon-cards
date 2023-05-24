import { Route } from "vue-router";
import { Store } from "vuex";
import { State } from "./types/DragonCards.interface";

declare module "@vue/runtime-core" {
  // provide typings for `this.$store`
  interface ComponentCustomProperties {
    $store: Store<State>;
    $route: Route;
  }
}
