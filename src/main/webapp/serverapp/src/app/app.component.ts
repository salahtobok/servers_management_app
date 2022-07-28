import {Component, OnInit} from '@angular/core';
import {ServerService} from "./service/server.service";
import {BehaviorSubject, map, Observable, of, startWith} from "rxjs";
import {AppState} from "./interface/app-state";
import {CustomResponse} from "./interface/custom-response";
import {DataState} from "./emun/data-state.enum";
import {catchError} from "rxjs/operators";
import {Status} from "./emun/status.enum";
import {NgForm} from "@angular/forms";
import {Server} from "./interface/server";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  appState$!: Observable<AppState<CustomResponse>>;
  readonly DataState = DataState;
  readonly Status = Status;
  private filterSubject = new BehaviorSubject<string>('');
  // @ts-ignore
  private dataSubject = new BehaviorSubject<CustomResponse>(null);

  filterStatus$ = this.filterSubject.asObservable();

  filter_criteria = "ALL";

  private isLoading = new BehaviorSubject<boolean>(false);
  isLoading$ = this.isLoading.asObservable();

  constructor(private serverService: ServerService) {
  }


  ngOnInit(): void {
      this.appState$ = this.serverService.servers$.pipe(
      map(response => {
          this.dataSubject.next(response);
          return {dataState: DataState.LOADED_STATE, appData: response}
        }
      ),
      startWith({dataState: DataState.LOADING_STATE}),
      catchError((error: string) => {
        return of({dataState: DataState.ERROR_STATE, error})
      })
    )
  }


  pingServer(ipAddress: string): void {
    this.filterSubject.next(ipAddress);
    this.appState$ = this.serverService.ping$(ipAddress)
      .pipe(
        map(response => {
            // @ts-ignore
            const index = this.dataSubject.value.data.servers.findIndex(server => server.id === response.data.server.id)
            if (response.data.server) {
              this.dataSubject.value.data.servers[index] = response.data.server;
            }
            this.filterSubject.next('')
            return {dataState: DataState.LOADED_STATE, appData: this.dataSubject.value}
          }
        ),
        startWith({dataState: DataState.LOADED_STATE, appData: this.dataSubject.value}),
        catchError((error: string) => {
          return of({dataState: DataState.ERROR_STATE, error})
        })
      )
  }

  saveServer(serverForm: NgForm): void {
    this.isLoading.next(true)
    this.appState$ = this.serverService.save$(serverForm.value as Server)
      .pipe(
        map(response => {
          this.dataSubject.next(
            {...response, data:{servers:[response.data.server!,...this.dataSubject.value.data.servers]}}
          )
          // @ts-ignore
          document.getElementById('closeModal').click();
          serverForm.resetForm({status:this.Status.SERVER_DOWN});
          this.isLoading.next(false)

          return {dataState: DataState.LOADED_STATE, appData: this.dataSubject.value}
          }
        ),
        startWith({dataState: DataState.LOADED_STATE, appData: this.dataSubject.value}),
        catchError((error: string) => {
          this.isLoading.next(false)
          return of({dataState: DataState.ERROR_STATE, error})
        })
      )
  }

  filterServers(status: Status): void {
    console.log("*****************");
    this.appState$ = this.serverService.filter$(status, this.dataSubject.value)
      .pipe(
        map(response => {
            return {dataState: DataState.LOADED_STATE, appData: response}
          }
        ),
        startWith({dataState: DataState.LOADED_STATE, appData: this.dataSubject.value}),
        catchError((error: string) => {
          return of({dataState: DataState.ERROR_STATE, error})
        })
      )
  }
}
