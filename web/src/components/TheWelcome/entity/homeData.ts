export class homeData {

    _date
    _viewCount
    _voteCount
    _viewIncrease
    _voteIncrease


    constructor(date :Date, viewCount = 0, voteCount = 0, viewIncrease= 0, voteIncrease= 0) {
        this._viewCount = viewCount;
        this._voteCount = voteCount;
        this._viewIncrease = viewIncrease;
        this._voteIncrease = voteIncrease;
        this._date = date
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
    get date() {
        return this._date;
    }

    set date(value) {
        this._date = value;
    }
}