class ValidationError < StandardError
  def initialize(msg="Validation error")
    super
  end
end

class PreBlockValidationError < ValidationError
  def initialize(msg="PRE block validation error")
    super
  end
end

class PostBlockValidationError < ValidationError
  def initialize(msg="POST block validation error")
    super
  end
end

class InvariantError < StandardError
  def initialize(msg="Invariant error")
    super
  end
end

