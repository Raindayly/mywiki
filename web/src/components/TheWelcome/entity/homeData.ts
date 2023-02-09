export class homeData {

    private _viewCount
    private _voteCount
    private _viewIncrease
    private _voteIncrease


    constructor(viewCount = 0, voteCount = 0, viewIncrease= 0, voteIncrease= 0) {
        this._viewCount = viewCount;
        this._voteCount = voteCount;
        this._viewIncrease = viewIncrease;
        this._voteIncrease = voteIncrease;
    }


    get viewCount(): number {
        return this._viewCount;
    }

    set viewCount(value: number) {
        this._viewCount = value;
    }

    get voteCount(): number {
        return this._voteCount;
    }

    set voteCount(value: number) {
        this._voteCount = value;
    }

    get viewIncrease(): number {
        return this._viewIncrease;
    }

    set viewIncrease(value: number) {
        this._viewIncrease = value;
    }

    get voteIncrease(): number {
        return this._voteIncrease;
    }

    set voteIncrease(value: number) {
        this._voteIncrease = value;
    }
}