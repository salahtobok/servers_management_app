import {DataState} from "../emun/data-state.enum";

export interface AppState<T> {
  dataState : DataState;
  appData?: T;
  error?:string;
}
