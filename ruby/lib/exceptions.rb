class ValidationError < StandardError
  def initialize(msg="Validation error")
    super
  end
end

class InvariantError < StandardError
  def initialize(msg="Invariant error")
    super
  end
end
