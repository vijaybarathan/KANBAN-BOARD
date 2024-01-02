import { STAGES } from "./Stage";

export type BOARDS={
    boardId:number;
    boardName:string;
    boardOwnerName:string
    boardDescription:string;
    boardCreatedOn:string;
    boardMembers:string[]; 
    kanbanStageList:STAGES[]
}